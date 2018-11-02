package de.ihrigb.fwla.fwlacenter.display;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.ihrigb.fwla.fwlacenter.services.api.CurrentOperationsService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/display/activeOperation")
@RequiredArgsConstructor
public class ActiveOperationController {

	private final ActiveOperationService activeService;
	private final CurrentOperationsService currentService;

	@GetMapping
	public ResponseEntity<?> get() {
		return activeService.get().map(o -> {
			return ResponseEntity.ok(o);
		}).orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> set(@PathVariable("id") String id) {
		return currentService.get().stream().filter(o -> {
			return o.getId().equals(id);
		}).findFirst().map(o -> {
			activeService.set(o);
			return ResponseEntity.noContent().build();
		}).orElse(ResponseEntity.notFound().build());
	}
}
