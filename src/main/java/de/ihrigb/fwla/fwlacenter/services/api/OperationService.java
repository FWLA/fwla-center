package de.ihrigb.fwla.fwlacenter.services.api;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import de.ihrigb.fwla.fwlacenter.persistence.model.Operation;
import de.ihrigb.fwla.fwlacenter.persistence.model.Station;

public interface OperationService {

	void addOperation(Operation operation);

	Optional<Operation> getActiveOperation(Station station);

	List<Operation> getCurrentOperations(Station station);

	Set<Operation> getOperations();

	default boolean hasCurrentOperations(Station station) {
		return !getCurrentOperations(station).isEmpty();
	}

	void closeOperation(String id);
}
