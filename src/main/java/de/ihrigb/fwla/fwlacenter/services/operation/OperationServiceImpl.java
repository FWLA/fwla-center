package de.ihrigb.fwla.fwlacenter.services.operation;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import de.ihrigb.fwla.fwlacenter.persistence.model.Operation;
import de.ihrigb.fwla.fwlacenter.persistence.model.Station;
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

	private final Map<String, List<Operation>> currentOperations = new ConcurrentHashMap<>();
	private final Map<String, Operation> activeOperations = new ConcurrentHashMap<>();

	@Transactional
	@Scheduled(fixedRate = 60000)
	public void timeoutOperations() {
		log.debug("Scheduled timeout of operations.");
		operationRepository.streamByClosedFalse().filter(o -> {
			return Duration.between(o.getCreated(), Instant.now()).compareTo(properties.getTimeout()) > 0;
		}).forEach(o -> {
			log.info("Operation {} timed out. Closing it.", o.getId());
			closeOperation(o.getId());
		});
		Set<Operation> operationsToBeClosed = currentOperations.values().stream().flatMap(list -> list.stream())
				.filter(o -> {
					return Duration.between(o.getCreated(), Instant.now()).compareTo(properties.getTimeout()) > 0;
				}).collect(Collectors.toSet());

		operationsToBeClosed.forEach(o -> {
			closeOperation(o.getId());
		});
	}

	@Override
	public Set<Operation> getOperations() {
		return Collections.unmodifiableSet(
				currentOperations.values().stream().flatMap(x -> x.stream()).collect(Collectors.toSet()));
	}

	@Override
	public void addOperation(Operation operation) {
		Assert.notNull(operation, "Operation must not be null.");

		log.info("Adding operation {}.", operation.getId());

		if (!operation.isTraining()) {
			operationRepository.save(operation);
		}

		if (!operation.isTraining() && containsTraining()) {
			log.debug("Clear existing training due to new real operation.");
			clearTraining();
		}

		Set<Station> stations = OperationUtils.getStations(operation);
		stations.forEach(station -> {
			String stationId = station.getId();
			if (currentOperations.get(stationId) == null) {
				currentOperations.put(stationId, new ArrayList<>());
			}

			currentOperations.get(stationId).add(0, operation);
		});
		resetActiveOperation();
	}

	@Override
	public Optional<Operation> getActiveOperation(Station station) {
		log.trace("getActiveOperation()");
		return Optional.ofNullable(activeOperations.get(station.getId()));
	}

	@Override
	public List<Operation> getCurrentOperations(Station station) {
		log.trace("getCurrentOperations()");
		List<Operation> operations = currentOperations.get(station.getId());
		if (operations == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(operations);
	}

	@Transactional
	@Override
	public void closeOperation(String id) {
		Assert.notNull(id, "Operation ID must not be null.");

		log.debug("Attempt to close operation {}.", id);

		get(id).ifPresent(operation -> {
			log.info("Close operation {}.", id);
			operation.setClosed(true);
			if (!operation.isTraining()) {
				operationRepository.save(operation);
			}
		});

		currentOperations.values().forEach(list -> {
			list.removeIf(operation -> {
				return id.equals(operation.getId());
			});
		});
		activeOperations.values().removeIf(operation -> {
			return id.equals(operation.getId());
		});
		resetActiveOperation();
	}

	private Optional<Operation> get(String id) {
		Assert.notNull(id, "Operation ID must not be null.");

		log.trace("get({})", id);

		Optional<Operation> operation = currentOperations.values().stream().flatMap(l -> l.stream()).filter(o -> {
			return id.equals(o.getId());
		}).findFirst();
		if (operation.isPresent()) {
			return operation;
		}
		return operationRepository.findById(id);
	}

	private void resetActiveOperation() {
		log.trace("resetActiveOperation()");
		currentOperations.entrySet().forEach(entry -> {
			Optional<Operation> o = entry.getValue().stream().findFirst();

			if (!o.isPresent()) {
				activeOperations.remove(entry.getKey());
			} else {
				activeOperations.put(entry.getKey(), o.get());
			}
		});
	}

	private boolean containsTraining() {
		log.trace("containsTraining()");
		return currentOperations.values().stream().flatMap(list -> list.stream()).anyMatch(Operation::isTraining);
	}

	private void clearTraining() {
		log.trace("clearTraining()");
		currentOperations.values().forEach(list -> {
			list.removeIf(Operation::isTraining);
		});
		activeOperations.values().removeIf(Operation::isTraining);
	}
}
