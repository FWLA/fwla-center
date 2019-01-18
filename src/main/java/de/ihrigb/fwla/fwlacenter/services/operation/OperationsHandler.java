package de.ihrigb.fwla.fwlacenter.services.operation;

import org.springframework.stereotype.Component;

import de.ihrigb.fwla.fwlacenter.handling.api.Handler;
import de.ihrigb.fwla.fwlacenter.persistence.model.Operation;
import de.ihrigb.fwla.fwlacenter.services.api.OperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OperationsHandler implements Handler {

	private final OperationService operationsService;

	@Override
	public void handle(Operation operation) {
		log.debug("Handling new operation {}.", operation.getId());
		operationsService.addOperation(operation);
	}
}
