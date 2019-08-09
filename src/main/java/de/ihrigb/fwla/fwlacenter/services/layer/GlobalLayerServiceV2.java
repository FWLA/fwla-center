package de.ihrigb.fwla.fwlacenter.services.layer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.geojson.FeatureCollection;
import org.springframework.stereotype.Component;

import de.ihrigb.fwla.fwlacenter.services.api.geo.FeatureDetails;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerGroup;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerProvider;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerServiceV2;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerUpdateNotSupportedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class GlobalLayerServiceV2 implements LayerServiceV2 {

	private final Set<LayerProvider> layerProviders;

	@Override
	public List<LayerGroup> getLayerGroups() {

		log.trace("Get list of layergroups.");

		List<LayerGroup> result = layerProviders.stream().map(lp -> lp.getLayerGroups()).flatMap(l -> l.stream())
				.collect(Collectors.toList());

		result.forEach(layerGroup -> {
			layerGroup.sortLayers();
		});

		Collections.sort(result);

		return result;
	}

	@Override
	public FeatureCollection getFeatures(String layerId) {

		log.trace("Get features of layer id {}.", layerId);

		return getLayerProvider(layerId).map(lp -> lp.getFeatures(layerId)).orElseGet(() -> new FeatureCollection());
	}

	@Override
	public boolean isEditable(String layerId) {

		log.trace("isEditable of layer id {},", layerId);

		return getLayerProvider(layerId).map(lp -> lp.isEditable(layerId)).orElse(false);
	}

	@Override
	public void update(String layerId, FeatureCollection featureCollection) throws LayerUpdateNotSupportedException {

		log.trace("Update of layer id {}.", layerId);

		getLayerProvider(layerId).orElseThrow(() -> {
			log.warn("No provider found for layer {}.", layerId);
			return new LayerUpdateNotSupportedException(layerId);
		}).update(layerId, featureCollection);
	}

	@Override
	public Optional<FeatureDetails> getFeatureDetails(String layerId, String featureId) {

		log.trace("Get feature details of layer id {layerId} and feature id {}.", layerId, featureId);

		return getLayerProvider(layerId).map(lp -> lp.getFeatureDetails(layerId, featureId))
				.orElseGet(() -> Optional.empty());
	}

	private Optional<LayerProvider> getLayerProvider(String layerId) {
		return layerProviders.stream().filter(lp -> lp.supports(layerId)).findFirst();
	}
}
