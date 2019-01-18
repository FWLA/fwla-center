package de.ihrigb.fwla.fwlacenter.services.operation;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import de.ihrigb.fwla.fwlacenter.persistence.model.Operation;
import de.ihrigb.fwla.fwlacenter.persistence.repository.OperationRepository;
import de.ihrigb.fwla.fwlacenter.services.api.OperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableConfigurationProperties(OperationProperties.class)
@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService {

	private final OperationRepository operationRepository;
	private final OperationProperties properties;

	private Map<String, Operation> trainingOperations = new ConcurrentHashMap<>();
	private LinkedList<String> currentOperationIds = new LinkedList<>();
	private String activeOperation;

	@Transactional
	@Scheduled(fixedRate = 60000)
	public void timeoutOperations() {
		log.debug("Scheduled timeout of operations.");
		operationRepository.streamAll().filter(o -> {
			return Duration.between(o.getCreated(), Instant.now()).compareTo(properties.getTimeout()) > 0;
		}).forEach(o -> {
			log.info("Operation {} timed out. Closing it.", o.getId());
			closeOperation(o.getId());
		});
		trainingOperations.values().stream().filter(o -> {
			return Duration.between(o.getCreated(), Instant.now()).compareTo(properties.getTimeout()) > 0;
		}).forEach(o -> {
			log.info("Traingin Operation {} timed out. Closing it.", o.getId());
			closeOperation(o.getId());
		});
	}

	@Override
	public List<Operation> getOperations() {
		log.trace("getOperations()");
		ArrayList<Operation> operations = new ArrayList<>();
		operations.addAll(operationRepository.findAll());
		operations.addAll(trainingOperations.values());
		return operations;
	}

	@Override
	public Page<Operation> getOperations(Pageable pageable) {
		return operationRepository.findAll(pageable);
	}

	@Override
	public void addOperation(Operation operation) {
		Assert.notNull(operation, "Operation must not be null.");

		log.info("Adding operation {}.", operation.getId());

		String id = operation.getId();
		if (!operation.isTraining()) {
			operationRepository.save(operation);
		} else {
			trainingOperations.put(id, operation);
		}

		if (!operation.isTraining() && containsTraining()) {
			log.debug("Clear existing training due to new real operation.");
			clearTraining();
		}

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
		return get(activeOperation);
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

		return Optional.ofNullable(operationRepository.findById(id).orElse(trainingOperations.get(id)));
	}

	@Override
	public List<Operation> getCurrentOperations() {
		log.trace("getCurrentOperations()");
		return Collections.unmodifiableList(currentOperationIds.stream().map(id -> {
			return get(id);
		}).map(o -> o.orElse(null)).filter(Objects::nonNull).collect(Collectors.toList()));
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
			if (!operation.isTraining()) {
				operationRepository.save(operation);
			} else {
				trainingOperations.remove(id);
			}
		});

		currentOperationIds.remove(id);
		if (activeOperation != null && activeOperation.equals(id)) {
			log.debug("Active operation was closed, resetting.");
			activeOperation = null;
			resetActiveOperation();
		}
	}

	@Override
	public long getCount() {
		return operationRepository.count() + trainingOperations.size();
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
		return !trainingOperations.isEmpty();
	}

	private void clearTraining() {
		log.trace("clearTraining()");
		Set<String> trainingIds = trainingOperations.keySet();
		currentOperationIds.removeAll(trainingIds);
		if (trainingIds.contains(activeOperation)) {
			activeOperation = null;
		}
		trainingOperations.clear();
	}
}
