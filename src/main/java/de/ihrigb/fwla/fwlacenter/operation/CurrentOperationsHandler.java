package de.ihrigb.fwla.fwlacenter.operation;

import org.springframework.stereotype.Component;

import de.ihrigb.fwla.fwlacenter.handling.api.Handler;
import de.ihrigb.fwla.fwlacenter.services.api.CurrentOperationsService;
import de.ihrigb.fwla.fwlacenter.services.api.Operation;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CurrentOperationsHandler implements Handler {

	private final CurrentOperationsService currentOperationsService;

	@Override
	public void handle(Operation operation) {
		currentOperationsService.add(operation);
	}
}
