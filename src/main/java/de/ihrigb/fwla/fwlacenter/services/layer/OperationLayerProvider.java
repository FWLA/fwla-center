package de.ihrigb.fwla.fwlacenter.services.layer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.Point;
import org.springframework.stereotype.Component;

import de.ihrigb.fwla.fwlacenter.persistence.model.Operation;
import de.ihrigb.fwla.fwlacenter.persistence.repository.OperationRepository;
import de.ihrigb.fwla.fwlacenter.services.api.OperationService;
import de.ihrigb.fwla.fwlacenter.services.api.geo.FeatureDetails;
import de.ihrigb.fwla.fwlacenter.services.api.geo.Layer;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerGroup;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerProvider;
import de.ihrigb.fwla.fwlacenter.utils.GeoJsonUtils;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class OperationLayerProvider implements LayerProvider {

	private static final String iconColor = "red";

	private static final String operationLayerId = "operation";
	private static final String operationsLayerIdPrefix = "operations";

	private final OperationRepository operationRepository;
	private final OperationService operationService;

	@Override
	public boolean supports(String layerId) {
		return OperationLayerProvider.operationLayerId.equals(layerId)
				|| layerId != null && layerId.startsWith(OperationLayerProvider.operationsLayerIdPrefix);
	}

	@Override
	public FeatureCollection getFeatures(String layer) {
		if (!layer.startsWith(OperationLayerProvider.operationLayerId)) {
			return new FeatureCollection();
		}
		if (OperationLayerProvider.operationLayerId.equals(layer)) {
			return operationService.getActiveOperation().map(map()).map(f -> {
				FeatureCollection fc = new FeatureCollection();
				fc.add(f);
				return fc;
			}).orElseGet(() -> new FeatureCollection());
		}
		int year = Integer.parseInt(layer.substring(OperationLayerProvider.operationsLayerIdPrefix.length()));
		return operationRepository.findByYear(year).filter(o -> o.getLocation() != null)
				.filter(o -> o.getLocation().getCoordinate() != null).map(map()).filter(Objects::nonNull)
				.collect(GeoJsonUtils.collector());
	}

	@Override
	public List<LayerGroup> getLayerGroups() {

		LayerGroup currentOperationLayerGroup = new LayerGroup(OperationLayerProvider.operationLayerId,
				Collections.singletonList(new Layer(OperationLayerProvider.operationLayerId, "Einsatz")));
		LayerGroup operationsLayerGroup = new LayerGroup(OperationLayerProvider.operationsLayerIdPrefix,
				operationRepository.findYears().stream().map(year -> {
					return new Layer(OperationLayerProvider.operationsLayerIdPrefix + year, "Einsätze " + year);
				}).filter(Objects::nonNull).collect(Collectors.toList()));

		return Arrays.asList(currentOperationLayerGroup, operationsLayerGroup);
	}

	private Function<Operation, Feature> map() {
		return (operation) -> {
			return Optional.ofNullable(operation).map(o -> {
				Point point = GeoJsonUtils.toPoint(o.getLocation().getCoordinate());
				Feature feature = new Feature();
				feature.setGeometry(point);
				feature.setId(o.getId());
				feature.setProperty("name",
						Optional.ofNullable(o.getOperationKey()).map(ok -> ok.getKey()).orElse(operation.getCode()));
				feature.setProperty("color", OperationLayerProvider.iconColor);
				return feature;
			}).orElse(null);
		};
	}

	@Override
	public Optional<FeatureDetails> getFeatureDetails(String layer, String featureId) {
		if (!layer.startsWith(OperationLayerProvider.operationLayerId)) {
			return Optional.empty();
		}
		return operationRepository.findById(featureId).map(o -> {
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
			return new FeatureDetails(name, o.getMessage(), o.getLocation().getAddress());
		});
	}
}
