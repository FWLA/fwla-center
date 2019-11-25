package de.ihrigb.fwla.fwlacenter.services.layer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.springframework.stereotype.Component;

import de.ihrigb.fwla.fwlacenter.persistence.model.Roadblock;
import de.ihrigb.fwla.fwlacenter.persistence.repository.RoadblockRepository;
import de.ihrigb.fwla.fwlacenter.services.api.geo.FeatureDetails;
import de.ihrigb.fwla.fwlacenter.services.api.geo.Layer;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerGroup;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerGroupCategory;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerUpdateNotSupportedException;
import de.ihrigb.fwla.fwlacenter.utils.GeoJsonUtils;

@Component
public class RoadblockLayerProvider extends AbstractRepositoryLayerProviderAdapter<Roadblock, String> {

	private static final String layerId = "roadblock";
	private static final String layerName = "Straßensperrungen";
	private static final String iconColor = "red";

	public RoadblockLayerProvider(RoadblockRepository roadblockRepository) {
		super(roadblockRepository);
	}

	@Override
	public Optional<FeatureDetails> getFeatureDetails(String layerId, String featureId) {
		if (!RoadblockLayerProvider.layerId.equals(layerId)) {
			return Optional.empty();
		}

		return getRepository().findById(featureId).map(roadblock -> {
			return new FeatureDetails(roadblock.getInformation(), null, roadblock.getLocation().getAddress());
		});
	}

	@Override
	String getId(Roadblock t) {
		return t.getId();
	}

	@Override
	String getName(Roadblock t) {
		return t.getInformation();
	}

	@Override
	public List<LayerGroup> getLayerGroups() {
		if (getRepository().count() == 0) {
			return Collections.emptyList();
		}
		return Collections.singletonList(new LayerGroup(RoadblockLayerProvider.layerId,
				Collections.singletonList(new Layer(RoadblockLayerProvider.layerId, RoadblockLayerProvider.layerName)),
				LayerGroupCategory.INFO));
	}

	@Override
	public boolean supports(String layerId) {
		return RoadblockLayerProvider.layerId.equals(layerId);
	}

	@Override
	public boolean isEditable(String layerId) {
		return false;
	}

	@Override
	public void update(String layerId, FeatureCollection featureCollection) throws LayerUpdateNotSupportedException {
		throw new LayerUpdateNotSupportedException(layerId);
	}

	@Override
	Feature toFeature(Roadblock t) {
		return createFeature(t, GeoJsonUtils.toPoint(t.getLocation().getCoordinate()),
				RoadblockLayerProvider.iconColor);
	}
}
