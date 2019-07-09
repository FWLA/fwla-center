package de.ihrigb.fwla.fwlacenter.services.layer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import de.ihrigb.fwla.fwlacenter.persistence.model.RealEstate;
import de.ihrigb.fwla.fwlacenter.persistence.repository.RealEstateRepository;
import de.ihrigb.fwla.fwlacenter.services.api.geo.Feature;
import de.ihrigb.fwla.fwlacenter.services.api.geo.FeatureDetails;
import de.ihrigb.fwla.fwlacenter.services.api.geo.Layer;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerGroup;
import de.ihrigb.fwla.fwlacenter.services.api.geo.PointFeature;

/**
 * @deprecated 0.1.4
 */
@Deprecated
class RealEstateLayerAdapter extends AbstractLayerAdapter<RealEstate, String> {

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
		if (getRepository().count() == 0) {
			return Collections.emptyList();
		}
		return Collections.singletonList(
				new LayerGroup("realEstate", Collections.singletonList(new Layer("realEstate", "Objekte"))));
	}

	@Override
	Feature toFeature(RealEstate t) {
		return new PointFeature(t.getId(), t.getName(), t.getLocation().getCoordinate(), "green");
	}

	@Override
	public Optional<FeatureDetails> getFeatureDetails(String layer, String featureId) {
		if (!"realEstate".equals(layer)) {
			return Optional.empty();
		}

		return getRepository().findById(featureId).map(realEstate -> {
			return new FeatureDetails(realEstate.getName(), realEstate.getInformation(),
					realEstate.getLocation().getAddress());
		});
	}
}
