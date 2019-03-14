package de.ihrigb.fwla.fwlacenter.services.layer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import de.ihrigb.fwla.fwlacenter.persistence.model.Station;
import de.ihrigb.fwla.fwlacenter.persistence.repository.StationRepository;
import de.ihrigb.fwla.fwlacenter.services.api.geo.Feature;
import de.ihrigb.fwla.fwlacenter.services.api.geo.FeatureDetails;
import de.ihrigb.fwla.fwlacenter.services.api.geo.Layer;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerGroup;
import de.ihrigb.fwla.fwlacenter.services.api.geo.PointFeature;

class StationLayerAdapter extends AbstractLayerAdapter<Station, String> {

	StationLayerAdapter(StationRepository repository) {
		super(repository);
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
		return Collections.singletonList(
				new LayerGroup("stations", Collections.singletonList(new Layer("stations", "Standorte"))));
	}

	@Override
	Feature toFeature(Station t) {
		return new PointFeature(t.getId(), t.getName(), t.getLocation().getCoordinate(), "grey");
	}

	@Override
	public Optional<FeatureDetails> getFeatureDetails(String layer, String featureId) {
		if (!"stations".equals(layer)) {
			return Optional.empty();
		}

		return getRepository().findById(featureId).map(station -> {
			return new FeatureDetails(station.getName(), null, station.getLocation().getAddress());
		});
	}
}
