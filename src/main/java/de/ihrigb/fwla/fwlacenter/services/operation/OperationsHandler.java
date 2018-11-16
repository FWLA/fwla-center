package de.ihrigb.fwla.fwlacenter.services.operation;

import org.springframework.stereotype.Component;

import de.ihrigb.fwla.fwlacenter.handling.api.Handler;
import de.ihrigb.fwla.fwlacenter.services.api.Operation;
import de.ihrigb.fwla.fwlacenter.services.api.OperationService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OperationsHandler implements Handler {

	private final OperationService operationsService;

	@Override
	public void handle(Operation operation) {
		operationsService.addOperation(operation);
	}
}
