package de.ihrigb.fwla.fwlacenter.services.layer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.ihrigb.fwla.fwlacenter.persistence.model.Operation;
import de.ihrigb.fwla.fwlacenter.persistence.repository.OperationRepository;
import de.ihrigb.fwla.fwlacenter.services.api.OperationService;
import de.ihrigb.fwla.fwlacenter.services.api.geo.Feature;
import de.ihrigb.fwla.fwlacenter.services.api.geo.Layer;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerGroup;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerService;
import de.ihrigb.fwla.fwlacenter.services.api.geo.PointFeature;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class OperationLayerService implements LayerService {

	private final OperationRepository operationRepository;
	private final OperationService operationService;

	@Override
	public Set<? extends Feature> getFeatures() {
		return null;
	}

	@Override
	public Set<? extends Feature> getFeatures(String layer) {
		if (!layer.startsWith("operation")) {
			return Collections.emptySet();
		}
		if ("operation".equals(layer)) {
			return operationService.getActiveOperation().map(map()).map(f -> Collections.singleton(f))
					.orElse(Collections.emptySet());
		}
		int year = Integer.parseInt(layer.substring("operations".length()));
		return operationRepository.findByYear(year).filter(o -> o.getLocation() != null)
				.filter(o -> o.getLocation().getCoordinate() != null).map(map()).filter(Objects::nonNull)
				.collect(Collectors.toSet());
	}

	@Override
	public List<LayerGroup> getLayerGroups() {

		LayerGroup currentOperationLayerGroup = new LayerGroup("operation",
				Collections.singletonList(new Layer("operation", "Einsatz")));
		LayerGroup operationsLayerGroup = new LayerGroup("operations",
				operationRepository.findYears().stream().map(year -> {
					return new Layer("operations" + year, "Einsätze " + year);
				}).filter(Objects::nonNull).collect(Collectors.toList()));

		return Arrays.asList(currentOperationLayerGroup, operationsLayerGroup);
	}

	private Function<Operation, Feature> map() {
		return (operation) -> {
			return Optional.ofNullable(operation).map(o -> {
				String name = null;
				if (o.getOperationKey() != null) {
					name = o.getOperationKey().getKey();
				}
				if (name == null) {
					name = o.getCode();
				}
				if (name == null) {
					name = o.getMessage();
				}
				return new PointFeature(name, o.getMessage(), o.getLocation(), "red");
			}).orElse(null);
		};
	}
}
