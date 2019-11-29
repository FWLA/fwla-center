package de.ihrigb.fwla.fwlacenter.web;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.ihrigb.fwla.fwlacenter.persistence.repository.StationRepository;
import de.ihrigb.fwla.fwlacenter.services.api.DisplayService;
import de.ihrigb.fwla.fwlacenter.web.model.DisplayStateDTO;
import lombok.RequiredArgsConstructor;

@Transactional
@RestController
@RequestMapping("/v1/display")
@RequiredArgsConstructor
public class DisplayController {

	private final DisplayService displayService;
	private final StationRepository stationRepository;

	@GetMapping("/{stationId}")
	public ResponseEntity<?> getDisplay(@PathVariable("stationId") String stationId) {
		return stationRepository.findById(stationId).map(station -> {
			return ResponseEntity.ok(new DisplayStateDTO(displayService.getDisplayState(station)));
		}).orElseGet(() -> {
			return ResponseEntity.notFound().build();
		});
	}
}
