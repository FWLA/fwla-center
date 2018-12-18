package de.ihrigb.fwla.fwlacenter.services.realestate;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import de.ihrigb.fwla.fwlacenter.handling.api.Processor;
import de.ihrigb.fwla.fwlacenter.persistence.model.RealEstate;
import de.ihrigb.fwla.fwlacenter.persistence.repository.RealEstateRepository;
import de.ihrigb.fwla.fwlacenter.services.api.EventLogService;
import de.ihrigb.fwla.fwlacenter.services.api.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RealEstateProcessor implements Processor {

	private final RealEstateRepository repository;
	private final EventLogService eventLogService;

	@Override
	public void process(Operation operation) {
		Assert.notNull(operation, "Operation must not be null.");
		
		log.debug("Processing operation {}.", operation.getId());

		if (operation.getObject() == null) {
			log.debug("Operation has no object set.");
			return;
		}

		if (operation.getRealEstate() != null) {
			log.debug("Operation already has a real estate set.");
			return;
		}

		Optional<RealEstate> optRealEstate = repository.findOneByKey(operation.getObject());
		if (optRealEstate.isPresent()) {
			RealEstate realEstate = optRealEstate.get();
			log.debug("Setting real estate {} to operation.", realEstate.getName());
			operation.setRealEstate(realEstate);
		} else {
			log.debug("Did not find real estate for key {}.", operation.getObject());
			eventLogService.error("Unable to find OperationKey with OBJECT '%s'.", operation.getObject());
		}
	}
}
