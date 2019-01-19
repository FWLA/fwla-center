package de.ihrigb.fwla.fwlacenter.services.layer;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.ihrigb.fwla.fwlacenter.persistence.model.Operation;
import de.ihrigb.fwla.fwlacenter.persistence.repository.OperationRepository;
import de.ihrigb.fwla.fwlacenter.services.api.OperationService;
import de.ihrigb.fwla.fwlacenter.services.api.geo.Feature;
import de.ihrigb.fwla.fwlacenter.services.api.geo.Layer;
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
		return operationRepository.findByYear(year).stream().map(map()).filter(Objects::nonNull)
				.collect(Collectors.toSet());
	}

	@Override
	public Set<Layer> getLayers() {
		return Stream
				.concat(Stream.of(new Layer("operation", "Einsatz")), operationRepository.findYears().stream()
						.map(year -> new Layer("operations" + year, "Einsätze " + year)).filter(Objects::nonNull))
				.collect(Collectors.toSet());
	}

	private Function<Operation, Feature> map() {
		return (operation) -> {
			return Optional.ofNullable(operation).map(o -> {
				return new PointFeature(o.getCode(), o.getMessage(), o.getLocation(), "red");
			}).orElse(null);
		};
	}
}
