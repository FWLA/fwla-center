package de.ihrigb.fwla.fwlacenter.services.operation;

import org.springframework.stereotype.Component;

import de.ihrigb.fwla.fwlacenter.handling.api.Processor;
import de.ihrigb.fwla.fwlacenter.persistence.repository.OperationKeyRepository;
import de.ihrigb.fwla.fwlacenter.services.api.Operation;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OperationKeyProcessor implements Processor {

	private final OperationKeyRepository repository;

	@Override
	public void process(Operation operation) {
		if (operation.getCode() == null || operation.getOperationKey() != null) {
			return;
		}

		repository.findOneByCode(operation.getCode()).ifPresent(operationKey -> {
			operation.setOperationKey(operationKey);
		});
	}
}
