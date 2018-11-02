package de.ihrigb.fwla.fwlacenter.services.api;

import java.util.List;

public interface CurrentOperationsService {
	List<Operation> get();
	void add(Operation operation);
	default void remove(Operation operation) {
		remove(operation.getId());
	}
	void remove(String id);
	void clear();
}
