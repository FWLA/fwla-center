package de.ihrigb.fwla.fwlacenter.web;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.ihrigb.fwla.fwlacenter.services.api.geo.Feature;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerService;
import de.ihrigb.fwla.fwlacenter.services.api.geo.PointFeature;
import de.ihrigb.fwla.fwlacenter.web.model.FeatureDTO;
import de.ihrigb.fwla.fwlacenter.web.model.LayerDTO;
import de.ihrigb.fwla.fwlacenter.web.model.PointFeatureDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/geo/layers")
@RequiredArgsConstructor
public class LayerController {

	private static FeatureDTO map(Feature feature) {
		if (feature instanceof PointFeature) {
			return new PointFeatureDTO((PointFeature) feature);
		}
		return new FeatureDTO(feature);
	}

	private final LayerService layerService;

	@GetMapping
	public ResponseEntity<Set<LayerDTO>> getLayers() {
		return ResponseEntity
				.ok(layerService.getLayers().stream().map(layer -> new LayerDTO(layer)).collect(Collectors.toSet()));
	}

	@GetMapping("/{layer}")
	public ResponseEntity<Set<? extends FeatureDTO>> getFeatures(@PathVariable("layer") String layer) {
		return ResponseEntity
				.ok(layerService.getFeatures(layer).stream().map(feature -> map(feature)).collect(Collectors.toSet()));
	}

}
