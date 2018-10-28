package de.ihrigb.fwla.fwlacenter.operation.api;

import java.util.List;

import de.ihrigb.fwla.fwlacenter.operation.Operation;

public interface CurrentOperationsService {
	List<Operation> get();
	void add(Operation operation);
	default void remove(Operation operation) {
		remove(operation.getId());
	}
	void remove(String id);
	void clear();
}
