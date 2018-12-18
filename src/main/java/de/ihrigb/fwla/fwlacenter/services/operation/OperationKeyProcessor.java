package de.ihrigb.fwla.fwlacenter.services.operation;

import java.util.Optional;

import org.springframework.stereotype.Component;

import de.ihrigb.fwla.fwlacenter.handling.api.Processor;
import de.ihrigb.fwla.fwlacenter.persistence.model.OperationKey;
import de.ihrigb.fwla.fwlacenter.persistence.repository.OperationKeyRepository;
import de.ihrigb.fwla.fwlacenter.services.api.EventLogService;
import de.ihrigb.fwla.fwlacenter.services.api.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OperationKeyProcessor implements Processor {

	private final OperationKeyRepository repository;
	private final EventLogService eventLogService;

	@Override
	public void process(Operation operation) {
		log.trace("process(Operation: {})", operation.getId());

		if (operation.getCode() == null) {
			log.debug("Operation has no code to be mapped.");
			return;
		}

		if (operation.getOperationKey() != null) {
			log.debug("Operation already has an operation key set.");
		}

		Optional<OperationKey> optOperationKey = repository.findOneByCode(operation.getCode());
		if (optOperationKey.isPresent()) {
			OperationKey operationKey = optOperationKey.get();
			log.debug("Found operation key {}.", operationKey.getKey());
			operation.setOperationKey(operationKey);
		} else {
			log.debug("Did not find an operation key for code {}.", operation.getCode());
			eventLogService.error("Unable to find OperationKey with CODE '%s'.", operation.getCode());
		}
	}
}
