package de.ihrigb.fwla.fwlacenter.web;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.ihrigb.fwla.fwlacenter.services.api.geo.Feature;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerService;
import de.ihrigb.fwla.fwlacenter.services.api.geo.PointFeature;
import de.ihrigb.fwla.fwlacenter.web.model.FeatureDTO;
import de.ihrigb.fwla.fwlacenter.web.model.FeatureDetailsDTO;
import de.ihrigb.fwla.fwlacenter.web.model.LayerGroupDTO;
import de.ihrigb.fwla.fwlacenter.web.model.PointFeatureDTO;
import lombok.RequiredArgsConstructor;

/**
 * @deprecated 0.1.4
 */
@Deprecated
@RestController
@RequestMapping("/v1/geo/layers")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LayerController {

	private static FeatureDTO map(Feature feature) {
		if (feature instanceof PointFeature) {
			return new PointFeatureDTO((PointFeature) feature);
		}
		return new FeatureDTO(feature);
	}

	private final LayerService layerService;

	@GetMapping
	public ResponseEntity<List<LayerGroupDTO>> getLayers() {
		return ResponseEntity.ok(layerService.getLayerGroups().stream().map(layerGroup -> new LayerGroupDTO(layerGroup))
				.collect(Collectors.toList()));
	}

	@GetMapping("/{layer}")
	public ResponseEntity<Set<? extends FeatureDTO>> getFeatures(@PathVariable("layer") String layer) {
		return ResponseEntity
				.ok(layerService.getFeatures(layer).stream().map(feature -> map(feature)).collect(Collectors.toSet()));
	}

	@GetMapping("/{layer}/{featureId}")
	public ResponseEntity<FeatureDetailsDTO> getFeatureDetails(@PathVariable("layer") String layer,
			@PathVariable("featureId") String featureId) {
		return layerService.getFeatureDetails(layer, featureId).map(fd -> {
			return ResponseEntity.ok(new FeatureDetailsDTO(fd));
		}).orElse(ResponseEntity.notFound().build());
	}

}
