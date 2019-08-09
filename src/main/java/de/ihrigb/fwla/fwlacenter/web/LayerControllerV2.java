package de.ihrigb.fwla.fwlacenter.web;

import java.util.List;
import java.util.stream.Collectors;

import org.geojson.FeatureCollection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerServiceV2;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerUpdateNotSupportedException;
import de.ihrigb.fwla.fwlacenter.web.model.FeatureDetailsDTO;
import de.ihrigb.fwla.fwlacenter.web.model.LayerGroupDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

	@GetMapping("/{layerId}")
	public ResponseEntity<FeatureCollection> getFeatures(@PathVariable("layerId") String layerId) {
		return ResponseEntity.ok(layerService.getFeatures(layerId));
	}

	@Transactional
	@PatchMapping("/{layerId}")
	public ResponseEntity<FeatureCollection> patchFeatures(@PathVariable("layerId") String layerId,
			@RequestBody FeatureCollection featureCollection) {
		if (!layerService.isEditable(layerId)) {
			log.warn("Layer {} is not editable.", layerId);
			return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
		}
		try {
			layerService.update(layerId, featureCollection);
		} catch (LayerUpdateNotSupportedException e) {
			log.warn("Exception during layer update.", e);
			return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
		}
		return getFeatures(layerId);
	}

	@GetMapping("/{layerId}/{featureId}")
	public ResponseEntity<FeatureDetailsDTO> getFeatureDetails(@PathVariable("layerId") String layerId,
			@PathVariable("featureId") String featureId) {
		return layerService.getFeatureDetails(layerId, featureId).map(fd -> {
			return ResponseEntity.ok(new FeatureDetailsDTO(fd));
		}).orElse(ResponseEntity.notFound().build());
	}
}
