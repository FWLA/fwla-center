package de.ihrigb.fwla.fwlacenter.operation;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.ihrigb.fwla.fwlacenter.handling.api.HandlerChain;
import de.ihrigb.fwla.fwlacenter.services.api.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/operations/training")
@RequiredArgsConstructor
public class TrainingOperationController {

	private final HandlerChain handlerChain;

	@PostMapping
	public ResponseEntity<?> createTrainingOperation(@RequestBody Operation operation) {
		operation.setTraining(true);
		operation.setId("training-" + UUID.randomUUID().toString());

		handlerChain.handle(operation);
		return ResponseEntity.noContent().build();
	}
}
