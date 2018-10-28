package de.ihrigb.fwla.fwlacenter.display;

import java.util.Optional;

import org.springframework.stereotype.Component;

import de.ihrigb.fwla.fwlacenter.operation.Operation;

@Component
public class ActiveOperationService {

	private Operation activeOperation;

	public void set(Operation operation) {
		this.activeOperation = operation;
	}

	public Optional<Operation> get() {
		return Optional.ofNullable(activeOperation);
	}
}
