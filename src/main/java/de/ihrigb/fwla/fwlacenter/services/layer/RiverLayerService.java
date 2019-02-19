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

import de.ihrigb.fwla.fwlacenter.api.Coordinate;
import de.ihrigb.fwla.fwlacenter.persistence.model.River;
import de.ihrigb.fwla.fwlacenter.persistence.repository.RiverSectorRepository;
import de.ihrigb.fwla.fwlacenter.services.api.geo.Feature;
import de.ihrigb.fwla.fwlacenter.services.api.geo.FeatureDetails;
import de.ihrigb.fwla.fwlacenter.services.api.geo.Layer;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerGroup;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerService;
import de.ihrigb.fwla.fwlacenter.services.api.geo.PointFeature;
import de.ihrigb.fwla.fwlacenter.services.river.CachingWSVRestServiceClient;
import de.ihrigb.fwla.fwlacenter.services.river.model.Fehlkilometer;
import de.ihrigb.fwla.fwlacenter.services.river.model.GeocodierungQuery;
import de.ihrigb.fwla.fwlacenter.services.river.model.GeocodierungResult;
import de.ihrigb.fwla.fwlacenter.services.river.model.RootResult;
import de.ihrigb.fwla.fwlacenter.services.river.model.GeocodierungQuery.Stationierung;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RiverLayerService implements LayerService {

	private static Pattern layerIdPattern = Pattern.compile("river-(?<bwastrid>\\d+)");

	private static Optional<String> extractBWaStrId(String layerId) {
		Matcher matcher = layerIdPattern.matcher(layerId);
		if (!matcher.matches()) {
			return Optional.empty();
		}
		return Optional.ofNullable(matcher.group("bwastrid"));
	}

	private static boolean isInFehlkilometer(float km, List<Fehlkilometer> fehlkilometer) {
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
			return new Layer("river-" + river.getBWaStrId(), river.getName());
		}).collect(Collectors.toList());

		return Collections.singletonList(new LayerGroup("Wasserstraßen", layers));
	}

	@Override
	public Set<? extends Feature> getFeatures(String layer) {
		if (layer == null || !layer.startsWith("river-")) {
			return Collections.emptySet();
		}

		Optional<String> optBWaStrId = RiverLayerService.extractBWaStrId(layer);
		if (!optBWaStrId.isPresent()) {
			return Collections.emptySet();
		}

		String bWaStrId = optBWaStrId.get();
		River river = River.ofBWaStrId(bWaStrId);
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
		kmsList.removeIf(k -> RiverLayerService.isInFehlkilometer(k, fehlkilometer));

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
			return new PointFeature(String.format(Locale.US, "%f", kmsList.get(res.getQid())), coordinate, "blue");
		}).collect(Collectors.toSet());
	}

	@Override
	public Optional<FeatureDetails> getFeatureDetails(String layer, String featureId) {
		if (!layer.startsWith("river-")) {
			return Optional.empty();
		}

		Optional<String> optBWaStrId = RiverLayerService.extractBWaStrId(layer);
		return optBWaStrId.map(bWaStrId -> {
			River river = River.ofBWaStrId(bWaStrId);
			return new FeatureDetails(river.getName(),
					String.format(Locale.GERMANY, "Kilometer %f", Float.parseFloat(featureId)), null);
		});
	}
}
