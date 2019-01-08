package de.ihrigb.fwla.fwlacenter.services.api;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OperationService {

	List<Operation> getOperations();

	/**
	 * List all operations paged.
	 *
	 * @param pageable the page request
	 * @return page of operations
	 */
	Page<Operation> getOperations(Pageable pageable);

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

	/**
	 * Get the total amount of operations.
	 *
	 * @return count of operations
	 */
	long getCount();
}
