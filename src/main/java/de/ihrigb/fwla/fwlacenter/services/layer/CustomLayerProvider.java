package de.ihrigb.fwla.fwlacenter.services.layer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.geojson.FeatureCollection;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.ihrigb.fwla.fwlacenter.persistence.model.MapLayer;
import de.ihrigb.fwla.fwlacenter.persistence.repository.MapLayerRepository;
import de.ihrigb.fwla.fwlacenter.services.api.geo.FeatureDetails;
import de.ihrigb.fwla.fwlacenter.services.api.geo.Layer;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerGroup;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerUpdateNotSupportedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
		LayerGroup lg = new LayerGroup();
		lg.setName("custom");
		lg.setLayers(mapLayerRepository.findAll().stream().map(mapLayer -> {
			return new Layer(mapLayerIdPrefix + mapLayer.getId(), mapLayer.getName(), true);
		}).collect(Collectors.toList()));
		return Collections.singletonList(lg);
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

		log.info("Updating layer {}.", layerId);

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
