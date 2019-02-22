package de.ihrigb.fwla.fwlacenter.services.layer;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import org.geojson.LngLatAlt;
import org.geojson.Point;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import de.ihrigb.fwla.fwlacenter.api.Coordinate;
import de.ihrigb.fwla.fwlacenter.persistence.model.RailwayCoordinateBox;
import de.ihrigb.fwla.fwlacenter.persistence.repository.RailwayCoordinateBoxRepository;
import de.ihrigb.fwla.fwlacenter.services.api.geo.Feature;
import de.ihrigb.fwla.fwlacenter.services.api.geo.FeatureDetails;
import de.ihrigb.fwla.fwlacenter.services.api.geo.Layer;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerGroup;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerService;
import de.ihrigb.fwla.fwlacenter.services.api.geo.PointFeature;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RailwayLayerService implements LayerService {

	static final String LAYER_GROUP_NAME = "Bahn";
	static final String LAYER_ID = "railway";
	static final String LAYER_NAME = "Bahn";
	private static final String geoJsonFile = "DB-markerPosts.geojson";

	private static PointFeature map(org.geojson.Feature feature) {
		Coordinate coordinate = Coordinate.of((Point) feature.getGeometry());
		return new PointFeature(feature.getProperty("id"), coordinate, "violet");
	}

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

	public RailwayLayerService(RailwayCoordinateBoxRepository repository) {
		this.repository = repository;
		this.featureCache = Caffeine.newBuilder().expireAfterWrite(Duration.ofDays(365)).build();
	}

	@Override
	public List<LayerGroup> getLayerGroups() {
		if (repository.count() == 0) {
			return Collections.emptyList();
		}
		return Collections
				.singletonList(new LayerGroup(LAYER_GROUP_NAME, Collections.singletonList(new Layer(LAYER_ID, LAYER_NAME))));
	}

	@Override
	public Set<? extends Feature> getFeatures(String layer) {
		if (!LAYER_ID.equals(layer)) {
			return Collections.emptySet();
		}
		List<RailwayCoordinateBox> railwayCoordinateBoxes = repository.findAll();
		if (railwayCoordinateBoxes.isEmpty()) {
			return Collections.emptySet();
		}

		Set<PointFeature> features = new HashSet<>();

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
				org.geojson.Feature feature = mapper.readValue(parser, org.geojson.Feature.class);
				if (isWithinBox(feature, railwayCoordinateBoxes)) {
					doCache(feature);
					features.add(map(feature));
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

		org.geojson.Feature feature = featureCache.getIfPresent(featureId);
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

	private void doCache(org.geojson.Feature feature) {
		if (!feature.getProperties().containsKey("id")) {
			log.warn("Feature not cachable.");
			return;
		}
		featureCache.put(feature.getProperty("id"), feature);
	}
}
