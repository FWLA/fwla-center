package de.ihrigb.fwla.fwlacenter.services.layer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.geojson.Feature;
import org.springframework.stereotype.Component;

import de.ihrigb.fwla.fwlacenter.persistence.model.Station;
import de.ihrigb.fwla.fwlacenter.persistence.repository.StationRepository;
import de.ihrigb.fwla.fwlacenter.services.api.geo.FeatureDetails;
import de.ihrigb.fwla.fwlacenter.services.api.geo.Layer;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerGroup;
import de.ihrigb.fwla.fwlacenter.utils.GeoJsonUtils;

@Component
public class StationLayerProvider extends AbstractLayerProviderAdapter<Station, String> {

	private static final String layerId = "stations";
	private static final String layerName = "Standorte";
	private static final String iconColor = "grey";

	public StationLayerProvider(StationRepository stationRepository) {
		super(stationRepository);
	}

	@Override
	public Optional<FeatureDetails> getFeatureDetails(String layerId, String featureId) {
		if (!StationLayerProvider.layerId.equals(layerId)) {
			return Optional.empty();
		}

		return getRepository().findById(featureId).map(station -> {
			return new FeatureDetails(station.getName(), null, station.getLocation().getAddress());
		});
	}

	@Override
	String getId(Station t) {
		return t.getId();
	}

	@Override
	String getName(Station t) {
		return t.getName();
	}

	@Override
	public List<LayerGroup> getLayerGroups() {
		if (getRepository().count() == 0) {
			return Collections.emptyList();
		}
		return Collections.singletonList(new LayerGroup(StationLayerProvider.layerId,
				Collections.singletonList(new Layer(StationLayerProvider.layerId, StationLayerProvider.layerName))));
	}

	@Override
	public boolean supports(String layerId) {
		return StationLayerProvider.layerId.equals(layerId);
	}

	@Override
	Feature toFeature(Station t) {
		return createFeature(t, GeoJsonUtils.toPoint(t.getLocation().getCoordinate()),
				StationLayerProvider.iconColor);
	}
}
