package de.ihrigb.fwla.fwlacenter.services.layer;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.ihrigb.fwla.fwlacenter.services.api.geo.Feature;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerGroup;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerService;

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
	public Set<? extends Feature> getFeatures() {
		return layerServices.stream().flatMap(ls -> ls.getFeatures().stream()).collect(Collectors.toSet());
	}

	@Override
	public Set<? extends Feature> getFeatures(String layer) {
		return layerServices.stream().flatMap(ls -> ls.getFeatures(layer).stream()).collect(Collectors.toSet());
	}
}
