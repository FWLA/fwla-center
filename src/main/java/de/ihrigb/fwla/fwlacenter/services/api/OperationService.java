package de.ihrigb.fwla.fwlacenter.services.api;

import java.util.List;
import java.util.Optional;

public interface OperationService {

	List<Operation> getOperations();

	void addOperation(Operation operation);

	void setActiveOperation(String id);

	Optional<Operation> getActiveOperation();

	default boolean hasActiveOperation() {
		return getActiveOperation().isPresent();
	}

	Optional<Operation> get(String id);

	List<Operation> getCurrentOperations();

	default boolean hasCurrentOperations() {
		return !getCurrentOperations().isEmpty();
	}

	void closeOperation(String id);
}
