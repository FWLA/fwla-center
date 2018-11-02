package de.ihrigb.fwla.fwlacenter.services.operation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import de.ihrigb.fwla.fwlacenter.services.api.CurrentOperationsService;
import de.ihrigb.fwla.fwlacenter.services.api.Operation;

@Component
public class CurrentOperationServiceImpl implements CurrentOperationsService {

	private final List<Operation> currentOperations = new ArrayList<>();

	@Override
	public List<Operation> get() {
		return Collections.unmodifiableList(currentOperations);
	}

	@Override
	public void add(Operation operation) {
		// make sure, that when a real operation comes in, all trainings are cancelled.
		if (!operation.isTraining()) {
			currentOperations.removeIf(o -> o.isTraining());
		}
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
