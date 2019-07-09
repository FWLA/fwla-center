package de.ihrigb.fwla.fwlacenter.services.layer;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.ihrigb.fwla.fwlacenter.services.api.geo.Feature;
import de.ihrigb.fwla.fwlacenter.services.api.geo.FeatureDetails;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerGroup;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerService;

/**
 * @deprecated 0.1.4
 */
@Deprecated
class CompositeLayerService implements LayerService {
	private final Set<? extends LayerService> layerServices;

	CompositeLayerService(LayerService... layerServices) {
		this.layerServices = Arrays.asList(layerServices).stream().collect(Collectors.toSet());
	}

	@Override
	public List<LayerGroup> getLayerGroups() {
		List<LayerGroup> layerGroups = layerServices.stream().flatMap(ls -> ls.getLayerGroups().stream())
				.collect(Collectors.toList());
		return layerGroups;
	}

	@Override
	public Set<? extends Feature> getFeatures(String layer) {
		return layerServices.stream().flatMap(ls -> ls.getFeatures(layer).stream()).collect(Collectors.toSet());
	}

	@Override
	public Optional<FeatureDetails> getFeatureDetails(String layer, String featureId) {
		return layerServices.stream().map(ls -> ls.getFeatureDetails(layer, featureId)).filter(Optional::isPresent).findFirst().orElse(Optional.empty());
	}
}
