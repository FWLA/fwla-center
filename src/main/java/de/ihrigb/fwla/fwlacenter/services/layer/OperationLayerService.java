package de.ihrigb.fwla.fwlacenter.services.layer;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import de.ihrigb.fwla.fwlacenter.persistence.model.Operation;
import de.ihrigb.fwla.fwlacenter.persistence.repository.OperationRepository;
import de.ihrigb.fwla.fwlacenter.services.api.geo.Feature;
import de.ihrigb.fwla.fwlacenter.services.api.geo.Layer;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerService;
import de.ihrigb.fwla.fwlacenter.services.api.geo.PointFeature;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class OperationLayerService implements LayerService {

	private final OperationRepository operationRepository;

	@Override
	public Set<? extends Feature> getFeatures() {
		return null;
	}

	@Override
	public Set<? extends Feature> getFeatures(String layer) {
		if (!layer.startsWith("operation")) {
			return Collections.emptySet();
		}
		int year = Integer.parseInt(layer.substring("operations".length()));
		return operationRepository.findByYear(year).stream().map(o -> map(o)).collect(Collectors.toSet());
	}

	@Override
	public Set<Layer> getLayers() {
		return operationRepository.findYears().stream().map(year -> new Layer("operations" + year, "Einsätze " + year))
				.collect(Collectors.toSet());
	}

	private Feature map(Operation operation) {
		return new PointFeature(operation.getCode(), operation.getMessage(), operation.getLocation(), "red");
	}
}
