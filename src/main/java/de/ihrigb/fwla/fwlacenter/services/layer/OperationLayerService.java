package de.ihrigb.fwla.fwlacenter.services.layer;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import de.ihrigb.fwla.fwlacenter.services.api.Operation;
import de.ihrigb.fwla.fwlacenter.services.api.OperationService;
import de.ihrigb.fwla.fwlacenter.services.api.geo.Feature;
import de.ihrigb.fwla.fwlacenter.services.api.geo.Layer;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerService;
import de.ihrigb.fwla.fwlacenter.services.api.geo.PointFeature;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class OperationLayerService implements LayerService {

	private final OperationService operationService;

	@Override
	public Set<? extends Feature> getFeatures() {
		return operationService.getOperations().stream().map(operation -> map(operation)).collect(Collectors.toSet());
	}

	@Override
	public Set<? extends Feature> getFeatures(String layer) {
		if ("operations".equals(layer)) {
			return getFeatures();
		}
		return Collections.emptySet();
	}

	@Override
	public Set<Layer> getLayers() {
		return Collections.singleton(new Layer("operations", "Einsätze"));
	}

	private Feature map(Operation operation) {
		return new PointFeature(operation.getCode(), operation.getMessage(), operation.getLocation(), "red");
	}
}
