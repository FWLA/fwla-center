package de.ihrigb.fwla.fwlacenter.services.realestate;

import java.util.Optional;

import org.springframework.stereotype.Component;

import de.ihrigb.fwla.fwlacenter.handling.api.Processor;
import de.ihrigb.fwla.fwlacenter.persistence.model.RealEstate;
import de.ihrigb.fwla.fwlacenter.persistence.repository.RealEstateRepository;
import de.ihrigb.fwla.fwlacenter.services.api.EventLogService;
import de.ihrigb.fwla.fwlacenter.services.api.Operation;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RealEstateProcessor implements Processor {

	private final RealEstateRepository repository;
	private final EventLogService eventLogService;

	@Override
	public void process(Operation operation) {
		if (operation.getObject() == null || operation.getRealEstate() != null) {
			return;
		}

		Optional<RealEstate> optRealEstate = repository.findOneByKey(operation.getObject());
		if (optRealEstate.isPresent()) {
			operation.setRealEstate(optRealEstate.get());
		} else {
			eventLogService.error("Unable to find OperationKey with OBJECT '%s'.", operation.getObject());
		}
	}
}
