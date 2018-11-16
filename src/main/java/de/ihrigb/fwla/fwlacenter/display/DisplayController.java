package de.ihrigb.fwla.fwlacenter.display;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.ihrigb.fwla.fwlacenter.services.api.Operation;
import de.ihrigb.fwla.fwlacenter.services.api.OperationService;
import de.ihrigb.fwla.fwlacenter.services.api.WeatherService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/display")
@RequiredArgsConstructor
public class DisplayController {

	private final OperationService operationService;
	private final Optional<WeatherService> weatherService;

	@GetMapping
	public ResponseEntity<?> getDisplay() {

		BaseDisplayState state;
		if (operationService.hasActiveOperation()) {
			Operation operation = operationService.getActiveOperation().orElse(null);
			state = new ActiveOperationDisplayState(operation);
			weatherService.ifPresent(ws -> {
				state.setWeather(ws.getWeather(operation.getLocation().getCoordinate()));
			});
		} else {
			state = new IdleOperationDisplayState();
		}

		return ResponseEntity.ok(state);
	}
}
