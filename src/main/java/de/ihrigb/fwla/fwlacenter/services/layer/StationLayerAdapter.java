package de.ihrigb.fwla.fwlacenter.services.layer;

import java.util.Collections;
import java.util.Set;

import de.ihrigb.fwla.fwlacenter.persistence.model.Station;
import de.ihrigb.fwla.fwlacenter.persistence.repository.StationRepository;
import de.ihrigb.fwla.fwlacenter.services.api.geo.Feature;
import de.ihrigb.fwla.fwlacenter.services.api.geo.Layer;
import de.ihrigb.fwla.fwlacenter.services.api.geo.PointFeature;

class StationLayerAdapter extends AbstractLayerAdapter<Station> {

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
	public Set<Layer> getLayers() {
		return Collections.singleton(new Layer("station", "Standorte"));
	}

	@Override
	Feature toFeature(Station t) {
		return new PointFeature(t.getName(), null, t.getLocation(), "grey");
	}
}
