package de.ihrigb.fwla.fwlacenter.display;

import java.util.Optional;

import org.springframework.stereotype.Component;

import de.ihrigb.fwla.fwlacenter.services.api.Operation;

@Component
public class ActiveOperationService {

	private Operation activeOperation;

	public void set(Operation operation) {
		this.activeOperation = operation;
	}

	public boolean isSet() {
		return get().isPresent();
	}

	public Optional<Operation> get() {
		return Optional.ofNullable(activeOperation);
	}
}
