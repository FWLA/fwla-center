package de.ihrigb.fwla.fwlacenter.services.layer;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.LngLatAlt;
import org.geojson.Point;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import de.ihrigb.fwla.fwlacenter.persistence.model.RailwayCoordinateBox;
import de.ihrigb.fwla.fwlacenter.persistence.repository.RailwayCoordinateBoxRepository;
import de.ihrigb.fwla.fwlacenter.services.api.geo.FeatureDetails;
import de.ihrigb.fwla.fwlacenter.services.api.geo.Layer;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerGroup;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RailwayLayerProvider extends AbstractLayerProvider {

	static final String LAYER_GROUP_NAME = "Bahn";
	static final String LAYER_ID = "railway";
	static final String LAYER_NAME = "Bahn";
	private static final String iconColor = "violet";
	private static final String geoJsonFile = "DB-markerPosts.geojson";

	private static boolean isWithinBox(org.geojson.Feature feature, List<RailwayCoordinateBox> railwayCoordinateBoxes) {
		if (!(feature.getGeometry() instanceof Point)) {
			return false;
		}

		LngLatAlt coords = ((Point) feature.getGeometry()).getCoordinates();
		double lat = coords.getLatitude();
		double lng = coords.getLongitude();

		for (RailwayCoordinateBox box : railwayCoordinateBoxes) {
			double minLat = Math.min(box.getUpperLeft().getLatitude(), box.getLowerRight().getLatitude());
			double maxLat = Math.max(box.getUpperLeft().getLatitude(), box.getLowerRight().getLatitude());
			double minLng = Math.min(box.getUpperLeft().getLongitude(), box.getLowerRight().getLongitude());
			double maxLng = Math.max(box.getUpperLeft().getLongitude(), box.getLowerRight().getLongitude());
			if (minLat <= lat && maxLat >= lat && minLng <= lng && maxLng >= lng) {
				return true;
			}
		}
		return false;
	}

	private final RailwayCoordinateBoxRepository repository;
	private final Cache<String, org.geojson.Feature> featureCache;

	public RailwayLayerProvider(RailwayCoordinateBoxRepository repository) {
		this.repository = repository;
		this.featureCache = Caffeine.newBuilder().expireAfterWrite(Duration.ofDays(365)).build();
	}

	@Override
	public List<LayerGroup> getLayerGroups() {
		if (repository.count() == 0) {
			return Collections.emptyList();
		}
		return Collections.singletonList(
				new LayerGroup(LAYER_GROUP_NAME, Collections.singletonList(new Layer(LAYER_ID, LAYER_NAME))));
	}

	@Override
	public boolean supports(String layerId) {
		return RailwayLayerProvider.LAYER_ID.equals(layerId);
	}

	@Override
	public FeatureCollection getFeatures(String layer) {
		if (!LAYER_ID.equals(layer)) {
			return new FeatureCollection();
		}
		List<RailwayCoordinateBox> railwayCoordinateBoxes = repository.findAll();
		if (railwayCoordinateBoxes.isEmpty()) {
			return new FeatureCollection();
		}

		FeatureCollection features = new FeatureCollection();

		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper(factory);
		try (InputStream in = getClass().getClassLoader().getResourceAsStream(geoJsonFile);
				JsonParser parser = factory.createParser(in);) {

			if (parser.nextToken() != JsonToken.START_OBJECT) {
				throw new IllegalStateException("Document did not start with START_OBJECT token.");
			}

			if (parser.nextToken() != JsonToken.FIELD_NAME) {
				throw new IllegalStateException("Fieldname expected.");
			}
			String fieldName = parser.getCurrentName();
			if (!"type".equals(fieldName)) {
				throw new IllegalStateException("Document did contain 'type' field.");
			}
			if (parser.nextToken() != JsonToken.VALUE_STRING) {
				throw new IllegalStateException("String value expected.");
			}
			String value = parser.getText();
			if (!"FeatureCollection".equals(value)) {
				throw new IllegalStateException("'Type' was not 'FeatureCollection'.");
			}

			if (parser.nextToken() != JsonToken.FIELD_NAME) {
				throw new IllegalStateException("Fieldname expected.");
			}
			fieldName = parser.getCurrentName();
			if (!"features".equals(fieldName)) {
				throw new IllegalStateException("Document did contain 'features' field.");
			}

			if (parser.nextToken() != JsonToken.START_ARRAY) {
				throw new IllegalStateException("START_ARRAY expected.");
			}

			while (parser.nextToken() == JsonToken.START_OBJECT) {
				Feature feature = mapper.readValue(parser, Feature.class);
				if (isWithinBox(feature, railwayCoordinateBoxes)) {
					standartize(feature);
					doCache(feature);
					features.add(feature);
				}
			}
		} catch (IOException e) {
			log.error("Exception while reading railway markers.", e);
			throw new RuntimeException(e);
		}

		return features;
	}

	@Override
	public Optional<FeatureDetails> getFeatureDetails(String layer, String featureId) {
		if (!LAYER_ID.equals(layer)) {
			return Optional.empty();
		}

		Feature feature = featureCache.getIfPresent(featureId);
		if (feature == null) {
			getFeatures(layer);
		}

		feature = featureCache.getIfPresent(featureId);
		if (feature == null) {
			return Optional.empty();
		}

		String trackName = feature.getProperty("railwayLineGeographicalName");
		double km = feature.getProperty("location");
		return Optional.of(new FeatureDetails(String.format(Locale.GERMANY, "KM %.2f", km),
				String.format("Bahnstrecke %s", trackName), null));
	}

	private void doCache(Feature feature) {
		if (!feature.getProperties().containsKey("id")) {
			log.warn("Feature not cachable.");
			return;
		}
		featureCache.put(feature.getProperty("id"), feature);
	}

	private void standartize(Feature feature) {
		double km = feature.getProperty("location");
		setNameProperty(feature, String.format(Locale.GERMANY, "KM %.2f", km));
		setColorProperty(feature, RailwayLayerProvider.iconColor);
		setHasDetailsProperty(feature, true);
	}
}
