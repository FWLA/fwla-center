package de.ihrigb.fwla.fwlacenter.display;

import org.springframework.stereotype.Component;

import de.ihrigb.fwla.fwlacenter.handling.api.Handler;
import de.ihrigb.fwla.fwlacenter.services.api.Operation;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DisplayHandler implements Handler {

	private final ActiveOperationService activeOperationService;

	@Override
	public void handle(Operation operation) {
		if (!activeOperationService.isSet()) {
			activeOperationService.set(operation);
		}
	}
}
