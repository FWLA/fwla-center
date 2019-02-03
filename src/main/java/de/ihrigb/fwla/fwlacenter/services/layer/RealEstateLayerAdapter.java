package de.ihrigb.fwla.fwlacenter.services.layer;

import java.util.Collections;
import java.util.List;

import de.ihrigb.fwla.fwlacenter.persistence.model.RealEstate;
import de.ihrigb.fwla.fwlacenter.persistence.repository.RealEstateRepository;
import de.ihrigb.fwla.fwlacenter.services.api.geo.Feature;
import de.ihrigb.fwla.fwlacenter.services.api.geo.Layer;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerGroup;
import de.ihrigb.fwla.fwlacenter.services.api.geo.PointFeature;

class RealEstateLayerAdapter extends AbstractLayerAdapter<RealEstate> {

	RealEstateLayerAdapter(RealEstateRepository repository) {
		super(repository);
	}

	@Override
	String getId(RealEstate t) {
		return t.getId();
	}

	@Override
	String getName(RealEstate t) {
		return t.getName();
	}

	@Override
	public List<LayerGroup> getLayerGroups() {
		return Collections
				.singletonList(new LayerGroup("realEstate", Collections.singleton(new Layer("realEstate", "Objekte"))));
	}

	@Override
	Feature toFeature(RealEstate t) {
		return new PointFeature(t.getName(), t.getInformation(), t.getLocation(), "green");
	}
}
