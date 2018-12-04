package de.ihrigb.fwla.fwlacenter.services.operation;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import de.ihrigb.fwla.fwlacenter.services.api.Operation;
import de.ihrigb.fwla.fwlacenter.services.api.OperationService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OperationServiceImpl implements OperationService {

	private Map<String, Operation> operations = new LinkedHashMap<>();
	private List<String> currentOperationIds = new LinkedList<>();
	private String activeOperation;

	@Scheduled(fixedRate = 60000)
	public void timeoutOperations() {
		log.debug("Scheduled timeout of operations.");
		operations.values().stream().filter(o -> {
			return Duration.between(o.getCreated(), Instant.now()).compareTo(Duration.ofMinutes(15)) > 0;
		}).forEach(o -> {
			log.debug("Operation {} timed out. Closing it.", o.getId());
			closeOperation(o.getId());
		});
	}

	@Override
	public void addOperation(Operation operation) {
		Assert.notNull(operation, "Operation must not be null.");

		if (!operation.isTraining() && containsTraining()) {
			clearTraining();
		}

		String id = operation.getId();
		operations.put(id, operation);
		currentOperationIds.add(id);
		resetActiveOperation();
	}

	@Override
	public void setActiveOperation(String id) {
		Assert.notNull(id, "Operation ID must not be null.");

		if (!currentOperationIds.contains(id)) {
			log.debug("Tried to set an active operation of unknown id.");
			return;
		}

		activeOperation = id;
	}

	@Override
	public Optional<Operation> getActiveOperation() {
		if (activeOperation == null) {
			return Optional.empty();
		}
		return Optional.ofNullable(operations.get(activeOperation));
	}

	@Override
	public boolean hasActiveOperation() {
		return activeOperation != null;
	}

	@Override
	public Optional<Operation> get(String id) {
		Assert.notNull(id, "Operation ID must not be null.");

		return Optional.ofNullable(operations.get(id));
	}

	@Override
	public List<Operation> getCurrentOperations() {
		return Collections.unmodifiableList(currentOperationIds.stream().map(id -> {
			return operations.get(id);
		}).collect(Collectors.toList()));
	}

	@Override
	public boolean hasCurrentOperations() {
		return !currentOperationIds.isEmpty();
	}

	@Override
	public void closeOperation(String id) {
		Assert.notNull(id, "Operation ID must not be null.");

		get(id).ifPresent(operation -> {
			operation.setClosed(true);
		});

		currentOperationIds.remove(id);
		if (activeOperation != null && activeOperation.equals(id)) {
			activeOperation = null;
			resetActiveOperation();
		}
	}

	private void resetActiveOperation() {
		if (activeOperation == null && !currentOperationIds.isEmpty()) {
			activeOperation = currentOperationIds.get(0);
		}
	}

	private boolean containsTraining() {
		return operations.values().stream().filter(o -> o.isTraining()).count() > 0;
	}

	private void clearTraining() {
		Set<String> trainingIds = operations.values().stream().filter(o -> o.isTraining()).map(o -> o.getId())
				.collect(Collectors.toSet());
		for (String id : trainingIds) {
			operations.remove(id);
		}
		currentOperationIds.removeAll(trainingIds);
		if (trainingIds.contains(activeOperation)) {
			activeOperation = null;
		}
	}
}
