package de.ihrigb.fwla.fwlacenter.services.layer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.geojson.FeatureCollection;
import org.springframework.stereotype.Component;

import de.ihrigb.fwla.fwlacenter.persistence.model.MapLayer;
import de.ihrigb.fwla.fwlacenter.persistence.repository.MapLayerRepository;
import de.ihrigb.fwla.fwlacenter.services.api.geo.FeatureDetails;
import de.ihrigb.fwla.fwlacenter.services.api.geo.Layer;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerGroup;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerGroupCategory;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerUpdateNotSupportedException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomLayerProvider extends AbstractLayerProvider {

	private final static String mapLayerIdPrefix = "custom-";

	private static String extractId(String layerId) {
		return layerId.substring(mapLayerIdPrefix.length());
	}

	private final MapLayerRepository mapLayerRepository;

	@Override
	public List<LayerGroup> getLayerGroups() {
		return mapLayerRepository.findAll().stream().map(mapLayer -> {
			String layerId = mapLayerIdPrefix + mapLayer.getId();
			Layer layer = new Layer(layerId, mapLayer.getName());
			return new LayerGroup(mapLayerIdPrefix, Collections.singletonList(layer),
					LayerGroupCategory.of(mapLayer.getCategory()));
		}).collect(Collectors.toList());
	}

	@Override
	public boolean supports(String layerId) {
		if (layerId == null || !layerId.startsWith(mapLayerIdPrefix)) {
			return false;
		}
		String id = extractId(layerId);
		return mapLayerRepository.existsById(id);
	}

	@Override
	public boolean isEditable(String layerId) {
		return supports(layerId);
	}

	@Override
	public void update(String layerId, FeatureCollection featureCollection) throws LayerUpdateNotSupportedException {
		if (!supports(layerId)) {
			throw new LayerUpdateNotSupportedException(layerId);
		}

		MapLayer mapLayer = mapLayerRepository.findById(extractId(layerId))
				.orElseThrow(() -> new LayerUpdateNotSupportedException(layerId));
		mapLayer.setGeoJson(featureCollection);
		mapLayerRepository.save(mapLayer);
	}

	@Override
	public FeatureCollection getFeatures(String layerId) {
		if (layerId == null || !layerId.startsWith(mapLayerIdPrefix)) {
			return new FeatureCollection();
		}
		String id = extractId(layerId);
		return mapLayerRepository.findById(id).map(mapLayer -> mapLayer.getGeoJson()).orElse(new FeatureCollection());
	}

	@Override
	public Optional<FeatureDetails> getFeatureDetails(String layerId, String featureId) {
		return Optional.empty();
	}
}
