package de.ihrigb.fwla.fwlacenter.web;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.ihrigb.fwla.fwlacenter.services.api.RoadblockService;
import de.ihrigb.fwla.fwlacenter.web.model.CoordinateBoxDTO;
import de.ihrigb.fwla.fwlacenter.web.model.RoadblockDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/services/roadblocks")
@RequiredArgsConstructor
public class RoadblockServiceController {

	private final RoadblockService roadblockService;

	@PostMapping
	public ResponseEntity<Set<RoadblockDTO>> getRoadblocksInBox(@RequestBody CoordinateBoxDTO coordinateBoxDTO) {
		return ResponseEntity.ok(roadblockService
				.getWithinBounds(coordinateBoxDTO.getSw().getApiModel(), coordinateBoxDTO.getNe().getApiModel())
				.stream().map(roadblock -> {
					return new RoadblockDTO(roadblock);
				}).collect(Collectors.toSet()));
	}
}
