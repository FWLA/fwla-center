package de.ihrigb.fwla.fwlacenter.services.operation;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
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
	private LinkedList<String> currentOperationIds = new LinkedList<>();
	private String activeOperation;

	@Scheduled(fixedRate = 60000)
	public void timeoutOperations() {
		log.debug("Scheduled timeout of operations.");
		operations.values().stream().filter(o -> {
			return Duration.between(o.getCreated(), Instant.now()).compareTo(Duration.ofMinutes(15)) > 0;
		}).forEach(o -> {
			log.info("Operation {} timed out. Closing it.", o.getId());
			closeOperation(o.getId());
		});
	}

	/*
	 * Note: "0 0 0 * * ?" is valid but "0 0 0 * * *" is invalid. Spring and
	 * Quartz document not mention this. Can use http://www.cronmaker.com/ to
	 * validate the Quartz Cron expression.
	 */
	@Scheduled(cron = "0 0 0 * * ?") // Run at 12:00:00 AM (midnight) every day
	public void purgeOperations() {
		log.debug("Purge operations closed && older than 7 days.");
		operations.values().stream().filter(Operation::isClosed).filter(o -> {
			return Duration.between(o.getCreated(), Instant.now()).compareTo(Duration.ofDays(7)) > 0;
		}).forEach(o -> {
			log.info("Purging operation {}.", o.getId());
			purgeOperation(o.getId());
		});
	}

	@Override
	public List<Operation> getOperations() {
		log.trace("getOperations()");
		return new ArrayList<>(operations.values());
	}

	@Override
	public void addOperation(Operation operation) {
		Assert.notNull(operation, "Operation must not be null.");

		log.info("Adding operation {}.", operation.getId());

		if (!operation.isTraining() && containsTraining()) {
			log.debug("Clear existing training due to new real operation.");
			clearTraining();
		}

		String id = operation.getId();
		operations.put(id, operation);
		currentOperationIds.addFirst(id);
		resetActiveOperation();
	}

	@Override
	public void setActiveOperation(String id) {
		Assert.notNull(id, "Operation ID must not be null.");

		log.info("Set active operation to {}.", id);

		if (!currentOperationIds.contains(id)) {
			log.debug("Tried to set an active operation of unknown id.");
			return;
		}

		activeOperation = id;
	}

	@Override
	public Optional<Operation> getActiveOperation() {
		log.trace("getActiveOperation()");

		if (activeOperation == null) {
			return Optional.empty();
		}
		return Optional.ofNullable(operations.get(activeOperation));
	}

	@Override
	public boolean hasActiveOperation() {
		log.trace("hasActiveOperation()");
		return activeOperation != null;
	}

	@Override
	public Optional<Operation> get(String id) {
		Assert.notNull(id, "Operation ID must not be null.");

		log.trace("get({})", id);

		return Optional.ofNullable(operations.get(id));
	}

	@Override
	public List<Operation> getCurrentOperations() {
		log.trace("getCurrentOperations()");
		return Collections.unmodifiableList(currentOperationIds.stream().map(id -> {
			return operations.get(id);
		}).collect(Collectors.toList()));
	}

	@Override
	public boolean hasCurrentOperations() {
		log.trace("hasCurrentOperations()");
		return !currentOperationIds.isEmpty();
	}

	@Override
	public void closeOperation(String id) {
		Assert.notNull(id, "Operation ID must not be null.");

		log.debug("Attempt to close operation {}.", id);

		get(id).ifPresent(operation -> {
			log.info("Close operation {}.", id);
			operation.setClosed(true);
		});

		currentOperationIds.remove(id);
		if (activeOperation != null && activeOperation.equals(id)) {
			log.debug("Active operation was closed, resetting.");
			activeOperation = null;
			resetActiveOperation();
		}
	}

	private void purgeOperation(String id) {
		operations.remove(id);
	}

	private void resetActiveOperation() {
		log.trace("resetActiveOperation()");
		if (!currentOperationIds.isEmpty()) {
			activeOperation = currentOperationIds.get(0);
			log.info("New active operation: {}.", activeOperation);
		}
	}

	private boolean containsTraining() {
		log.trace("containsTraining()");
		return operations.values().stream().filter(o -> o.isTraining()).count() > 0;
	}

	private void clearTraining() {
		log.trace("clearTraining()");
		Set<String> trainingIds = operations.values().stream().filter(o -> o.isTraining()).map(o -> o.getId())
				.collect(Collectors.toSet());
		for (String id : trainingIds) {
			log.debug("Removing training operation {}.", id);
			operations.remove(id);
		}
		currentOperationIds.removeAll(trainingIds);
		if (trainingIds.contains(activeOperation)) {
			activeOperation = null;
		}
	}
}
