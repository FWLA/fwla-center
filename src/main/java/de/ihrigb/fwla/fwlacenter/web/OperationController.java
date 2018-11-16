package de.ihrigb.fwla.fwlacenter.web;

import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.ihrigb.fwla.fwlacenter.handling.api.OperationChain;
import de.ihrigb.fwla.fwlacenter.services.api.Operation;
import de.ihrigb.fwla.fwlacenter.services.api.OperationService;
import de.ihrigb.fwla.fwlacenter.web.model.OperationDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/operations")
@RequiredArgsConstructor
public class OperationController {

	private final OperationService operationService;
	private final OperationChain OperationChain;

	@GetMapping("/active")
	public ResponseEntity<?> getActiveOperation() {
		return operationService.getActiveOperation().map(toDTOMapper()).map(dto -> ResponseEntity.ok(dto))
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/current")
	public ResponseEntity<?> getCurrentOperations() {
		return ResponseEntity
				.ok(operationService.getCurrentOperations().stream().map(toDTOMapper()).collect(Collectors.toList()));
	}

	@PostMapping("/training")
	public ResponseEntity<?> createTraining(@RequestBody OperationDTO dto) {
		Operation operation = fromDTOMapper().apply(dto);
		OperationChain.put(operation);
		return ResponseEntity.accepted().build();
	}

	@PatchMapping("/{id}/close")
	public ResponseEntity<?> close(@PathVariable("{id}") String id) {
		operationService.closeOperation(id);
		return ResponseEntity.accepted().build();
	}

	private Function<OperationDTO, Operation> fromDTOMapper() {
		return dto -> dto == null ? null : dto.getApiModel();
	}

	private Function<Operation, OperationDTO> toDTOMapper() {
		return operation -> operation == null ? null : new OperationDTO(operation);
	}
}
