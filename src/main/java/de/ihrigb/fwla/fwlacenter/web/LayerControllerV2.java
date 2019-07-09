package de.ihrigb.fwla.fwlacenter.web;

import java.util.List;
import java.util.stream.Collectors;

import org.geojson.FeatureCollection;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerServiceV2;
import de.ihrigb.fwla.fwlacenter.web.model.FeatureDetailsDTO;
import de.ihrigb.fwla.fwlacenter.web.model.LayerGroupDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v2/geo/layers")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LayerControllerV2 {

	private final LayerServiceV2 layerService;

	@GetMapping
	public ResponseEntity<List<LayerGroupDTO>> getLayers() {
		return ResponseEntity.ok(layerService.getLayerGroups().stream().map(layerGroup -> new LayerGroupDTO(layerGroup))
				.collect(Collectors.toList()));
	}

	@GetMapping("/{layer}")
	public ResponseEntity<FeatureCollection> getFeatures(@PathVariable("layer") String layer) {
		return ResponseEntity.ok(layerService.getFeatures(layer));
	}

	@GetMapping("/{layer}/{featureId}")
	public ResponseEntity<FeatureDetailsDTO> getFeatureDetails(@PathVariable("layer") String layer,
			@PathVariable("featureId") String featureId) {
		return layerService.getFeatureDetails(layer, featureId).map(fd -> {
			return ResponseEntity.ok(new FeatureDetailsDTO(fd));
		}).orElse(ResponseEntity.notFound().build());
	}
}
