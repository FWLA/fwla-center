package de.ihrigb.fwla.fwlacenter.services.realestate;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import de.ihrigb.fwla.fwlacenter.handling.api.Processor;
import de.ihrigb.fwla.fwlacenter.handling.core.ProcessorOrder;
import de.ihrigb.fwla.fwlacenter.persistence.model.RealEstate;
import de.ihrigb.fwla.fwlacenter.persistence.repository.RealEstateRepository;
import de.ihrigb.fwla.fwlacenter.services.api.EventLogService;
import de.ihrigb.fwla.fwlacenter.persistence.model.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(ProcessorOrder.REAL_ESTATE_PROCESSOR_ORDER)
@RequiredArgsConstructor
public class RealEstateProcessor implements Processor {

	private final RealEstateRepository repository;
	private final EventLogService eventLogService;

	@Override
	@Transactional(readOnly = true)
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

		Optional<RealEstate> optRealEstate = repository.findOneByName(operation.getObject());
		if (optRealEstate.isPresent()) {
			set(operation, optRealEstate.get());
		} else {
			optRealEstate = resolveByPattern(operation.getObject());
			if (optRealEstate.isPresent()) {
				set(operation, optRealEstate.get());
			} else {
				log.debug("Did not find real estate for key {}.", operation.getObject());
				eventLogService.error("Unable to find RealEstate with OBJECT '%s' (Coordinate %s).",
						operation.getObject(), Optional.ofNullable(operation.getLocation()).map(l -> l.getCoordinate())
								.map(c -> c.toString()).orElse("n/a"));
			}
		}
	}

	private void set(Operation operation, RealEstate realEstate) {
		log.debug("Setting real estate {} to operation.", realEstate.getName());
		operation.setRealEstate(realEstate);
		if (realEstate.getPattern() != null) {
			Pattern pattern = Pattern.compile(realEstate.getPattern());
			Matcher matcher = pattern.matcher(operation.getObject());
			if (matcher.matches()) {
				try {
					String additional = matcher.group(1);
					if (StringUtils.hasLength(additional)) {
						operation.setRealEstateAdditional(additional);
					}
				} catch (IndexOutOfBoundsException e) {
					log.debug("No group with additional information.");
				}
			}
		}
	}

	private Optional<RealEstate> resolveByPattern(String key) {
		try (Stream<RealEstate> stream = repository.streamAll()) {
			return stream.filter(realEstate -> {
				return key.matches(realEstate.getPattern());
			}).findFirst();
		}
	}
}
