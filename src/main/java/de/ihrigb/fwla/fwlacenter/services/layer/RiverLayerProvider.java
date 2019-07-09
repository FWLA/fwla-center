package de.ihrigb.fwla.fwlacenter.services.layer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.Point;
import org.springframework.stereotype.Component;

import de.ihrigb.fwla.fwlacenter.api.Coordinate;
import de.ihrigb.fwla.fwlacenter.persistence.model.River;
import de.ihrigb.fwla.fwlacenter.persistence.repository.RiverSectorRepository;
import de.ihrigb.fwla.fwlacenter.services.api.geo.FeatureDetails;
import de.ihrigb.fwla.fwlacenter.services.api.geo.Layer;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerGroup;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerProvider;
import de.ihrigb.fwla.fwlacenter.services.river.CachingWSVRestServiceClient;
import de.ihrigb.fwla.fwlacenter.services.river.model.Fehlkilometer;
import de.ihrigb.fwla.fwlacenter.services.river.model.GeocodierungQuery;
import de.ihrigb.fwla.fwlacenter.services.river.model.GeocodierungQuery.Stationierung;
import de.ihrigb.fwla.fwlacenter.utils.GeoJsonUtils;
import de.ihrigb.fwla.fwlacenter.services.river.model.GeocodierungResult;
import de.ihrigb.fwla.fwlacenter.services.river.model.RootResult;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RiverLayerProvider implements LayerProvider {

	private static final String iconColor = "blue";
	private static final String layerIdPrefix = "river-";
	private static final Pattern layerIdPattern = Pattern.compile(layerIdPrefix + "(?<bwastrid>\\d+)");

	private static Optional<String> extractBWaStrId(String layerId) {
		Matcher matcher = layerIdPattern.matcher(layerId);
		if (!matcher.matches()) {
			return Optional.empty();
		}
		return Optional.ofNullable(matcher.group("bwastrid"));
	}

	private static boolean isInFehlkilometer(float km, List<Fehlkilometer> fehlkilometer) {
		if (fehlkilometer == null) {
			return false;
		}
		for (Fehlkilometer fk : fehlkilometer) {
			if (fk.getKmVon() < km && fk.getKmBis() > km) {
				return true;
			}
		}
		return false;
	}

	private final RiverSectorRepository repository;
	private final CachingWSVRestServiceClient restClient;

	@Override
	public List<LayerGroup> getLayerGroups() {

		List<Layer> layers = repository.findRivers().stream().map(river -> {
			return new Layer(RiverLayerProvider.layerIdPrefix + river.getBWaStrId(), river.getName());
		}).collect(Collectors.toList());

		return Collections.singletonList(new LayerGroup("Wasserstraßen", layers));
	}

	@Override
	public boolean supports(String layerId) {
		return layerId != null && layerId.startsWith(RiverLayerProvider.layerIdPrefix);
	}

	@Override
	public FeatureCollection getFeatures(String layer) {
		if (layer == null || !layer.startsWith(RiverLayerProvider.layerIdPrefix)) {
			return new FeatureCollection();
		}

		Optional<String> optBWaStrId = RiverLayerProvider.extractBWaStrId(layer);
		if (!optBWaStrId.isPresent()) {
			return new FeatureCollection();
		}

		String bWaStrId = optBWaStrId.get();
		Optional<River> optRiver = River.ofBWaStrId(bWaStrId);
		if (!optRiver.isPresent()) {
			return new FeatureCollection();
		}
		River river = optRiver.get();
		Set<Float> km = new HashSet<>();

		// (1) get all river sectors of this river and add kms by interval to set.
		repository.findByRiver(river).forEach(riverSector -> {
			for (float i = riverSector.getFrom(); i <= riverSector.getTo(); i += riverSector.getInterval()) {
				km.add(i);
			}
		});

		// (2) put set into list and sort
		List<Float> kmsList = new ArrayList<>(km);
		Collections.sort(kmsList);

		// (3) get fehlkilometer
		List<Fehlkilometer> fehlkilometer = restClient
				.fehlkilometer(bWaStrId, kmsList.get(0), kmsList.get(kmsList.size() - 1)).getResult().get(0)
				.getFehlkilometer();

		// (4) remove from list, if within fehlkilometer
		if (fehlkilometer != null) {
			kmsList.removeIf(k -> RiverLayerProvider.isInFehlkilometer(k, fehlkilometer));
		}

		List<GeocodierungQuery> queries = new ArrayList<>();

		// (5) build queries with index as qid
		for (int i = 0; i < kmsList.size(); i++) {
			GeocodierungQuery query = new GeocodierungQuery();
			query.setQid(i);
			query.setBWaStrId(bWaStrId);
			Stationierung stationierung = new Stationierung();
			stationierung.setKmWert(kmsList.get(i));
			stationierung.setOffset(0);
			query.setStationierung(stationierung);
			queries.add(query);
		}

		// (6) execute queries
		RootResult<GeocodierungResult> result = restClient.geocode(queries);

		// (7) build features from results
		return result.getResult().stream().map(res -> {
			Coordinate coordinate = Coordinate.of(res.getGeometry());
			Point point = GeoJsonUtils.toPoint(coordinate);
			Feature feature = new Feature();
			feature.setGeometry(point);
			feature.setId(String.format(Locale.US, "%f", kmsList.get(res.getQid())));
			feature.setProperty("name", String.format(Locale.GERMANY, "KM %.2f", kmsList.get(res.getQid())));
			feature.setProperty("color", RiverLayerProvider.iconColor);
			return feature;
		}).collect(GeoJsonUtils.collector());
	}

	@Override
	public Optional<FeatureDetails> getFeatureDetails(String layer, String featureId) {
		if (!layer.startsWith(RiverLayerProvider.layerIdPrefix)) {
			return Optional.empty();
		}

		Optional<String> optBWaStrId = RiverLayerProvider.extractBWaStrId(layer);
		if (!optBWaStrId.isPresent()) {
			return Optional.empty();
		}

		return River.ofBWaStrId(optBWaStrId.get()).map(river -> {
			return new FeatureDetails(String.format(Locale.GERMANY, "Kilometer %.2f", Float.parseFloat(featureId)),
					river.getName(), null);
		});
	}
}
