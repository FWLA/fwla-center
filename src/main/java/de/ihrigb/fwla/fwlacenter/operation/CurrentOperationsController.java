package de.ihrigb.fwla.fwlacenter.operation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.ihrigb.fwla.fwlacenter.operation.api.CurrentOperationsService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/currentoperations")
@RequiredArgsConstructor
public class CurrentOperationsController {

	private final CurrentOperationsService service;

	@GetMapping
	public ResponseEntity<?> get() {
		return ResponseEntity.ok(service.get());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") String id) {
		service.remove(id);
		return ResponseEntity.noContent().build();
	}
}
