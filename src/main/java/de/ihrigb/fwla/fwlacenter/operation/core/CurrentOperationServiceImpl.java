package de.ihrigb.fwla.fwlacenter.operation.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import de.ihrigb.fwla.fwlacenter.operation.Operation;
import de.ihrigb.fwla.fwlacenter.operation.api.CurrentOperationsService;

@Component
public class CurrentOperationServiceImpl implements CurrentOperationsService {

	private final List<Operation> currentOperations = new ArrayList<>();

	@Override
	public List<Operation> get() {
		return Collections.unmodifiableList(currentOperations);
	}

	@Override
	public void add(Operation operation) {
		currentOperations.add(operation);
	}

	@Override
	public void clear() {
		currentOperations.clear();
	}

	@Override
	public void remove(String id) {
		currentOperations.removeIf(operation -> {
			return operation.getId().equals(id);
		});
	}
}
