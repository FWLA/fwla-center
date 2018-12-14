package de.ihrigb.fwla.fwlacenter.services.operation;

import java.util.Optional;

import org.springframework.stereotype.Component;

import de.ihrigb.fwla.fwlacenter.handling.api.Processor;
import de.ihrigb.fwla.fwlacenter.persistence.model.OperationKey;
import de.ihrigb.fwla.fwlacenter.persistence.repository.OperationKeyRepository;
import de.ihrigb.fwla.fwlacenter.services.api.EventLogService;
import de.ihrigb.fwla.fwlacenter.services.api.Operation;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OperationKeyProcessor implements Processor {

	private final OperationKeyRepository repository;
	private final EventLogService eventLogService;

	@Override
	public void process(Operation operation) {
		if (operation.getCode() == null || operation.getOperationKey() != null) {
			return;
		}

		Optional<OperationKey> optOperationKey = repository.findOneByCode(operation.getCode());
		if (optOperationKey.isPresent()) {
			operation.setOperationKey(optOperationKey.get());
		} else {
			eventLogService.error("Unable to find OperationKey with CODE '%s'.", operation.getCode());
		}
	}
}
