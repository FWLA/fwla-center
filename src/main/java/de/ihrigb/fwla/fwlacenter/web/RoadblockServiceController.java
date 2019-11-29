package de.ihrigb.fwla.fwlacenter.web;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.ihrigb.fwla.fwlacenter.persistence.repository.RoadblockRepository;
import de.ihrigb.fwla.fwlacenter.web.model.BasicRoadblockDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/services/roadblocks")
@RequiredArgsConstructor
public class RoadblockServiceController {

	private final RoadblockRepository roadblockRepository;

	@GetMapping
	public ResponseEntity<Set<BasicRoadblockDTO>> getRoadblocksInBox() {
		return ResponseEntity.ok(roadblockRepository.findAll().stream().map(roadblock -> {
			return new BasicRoadblockDTO(roadblock);
		}).collect(Collectors.toSet()));
	}
}
