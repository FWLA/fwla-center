package de.ihrigb.fwla.fwlacenter.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.ihrigb.fwla.fwlacenter.configuration.HomeProvider;
import de.ihrigb.fwla.fwlacenter.web.model.CoordinateDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/settings")
@RequiredArgsConstructor
public class SettingsController {

	private final HomeProvider homeProvider;

	@GetMapping("/home")
	public ResponseEntity<CoordinateDTO> home() {
		return ResponseEntity.ok(new CoordinateDTO(homeProvider.getHome()));
	}
}
