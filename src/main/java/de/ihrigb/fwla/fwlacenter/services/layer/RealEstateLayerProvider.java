package de.ihrigb.fwla.fwlacenter.services.layer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.geojson.Feature;
import org.springframework.stereotype.Component;

import de.ihrigb.fwla.fwlacenter.persistence.model.RealEstate;
import de.ihrigb.fwla.fwlacenter.persistence.repository.RealEstateRepository;
import de.ihrigb.fwla.fwlacenter.services.api.geo.FeatureDetails;
import de.ihrigb.fwla.fwlacenter.services.api.geo.Layer;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerGroup;
import de.ihrigb.fwla.fwlacenter.utils.GeoJsonUtils;

@Component
public class RealEstateLayerProvider extends AbstractLayerProviderAdapter<RealEstate, String> {

	private static final String layerId = "realEstate";
	private static final String layerName = "Objekte";
	private static final String iconColor = "green";

	public RealEstateLayerProvider(RealEstateRepository realEstateRepository) {
		super(realEstateRepository);
	}

	@Override
	public Optional<FeatureDetails> getFeatureDetails(String layerId, String featureId) {
		if (!RealEstateLayerProvider.layerId.equals(layerId)) {
			return Optional.empty();
		}

		return getRepository().findById(featureId).map(station -> {
			return new FeatureDetails(station.getName(), null, station.getLocation().getAddress());
		});
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
		return Collections.singletonList(new LayerGroup(RealEstateLayerProvider.layerId, Collections
				.singletonList(new Layer(RealEstateLayerProvider.layerId, RealEstateLayerProvider.layerName))));
	}

	@Override
	public boolean supports(String layerId) {
		return RealEstateLayerProvider.layerId.equals(layerId);
	}

	@Override
	Feature toFeature(RealEstate t) {
		return createFeature(t, GeoJsonUtils.toPoint(t.getLocation().getCoordinate()),
				RealEstateLayerProvider.iconColor);
	}
}
