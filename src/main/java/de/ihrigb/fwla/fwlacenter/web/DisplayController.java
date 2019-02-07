package de.ihrigb.fwla.fwlacenter.web;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.ihrigb.fwla.fwlacenter.services.api.DisplayService;
import de.ihrigb.fwla.fwlacenter.web.model.DisplayStateDTO;
import lombok.RequiredArgsConstructor;

@Transactional
@RestController
@RequestMapping("/v1/display")
@RequiredArgsConstructor
public class DisplayController {

	private final DisplayService displayService;

	@GetMapping
	public ResponseEntity<?> getDisplay() {
		return ResponseEntity.ok(new DisplayStateDTO(displayService.getDisplayState()));
	}
}
