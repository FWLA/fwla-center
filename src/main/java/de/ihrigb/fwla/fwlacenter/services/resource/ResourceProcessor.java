package de.ihrigb.fwla.fwlacenter.services.resource;

import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import de.ihrigb.fwla.fwlacenter.handling.api.Processor;
import de.ihrigb.fwla.fwlacenter.persistence.repository.ResourceRepository;
import de.ihrigb.fwla.fwlacenter.services.api.EventLogService;
import de.ihrigb.fwla.fwlacenter.services.api.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResourceProcessor implements Processor {

	private final ResourceRepository resourceRepository;
	private final EventLogService eventLogService;

	@Override
	public void process(Operation operation) {
		Assert.notNull(operation, "Operation must not be null.");

		log.debug("Processing operation {}.", operation.getId());

		if (operation.getResourceKeys() == null) {
			log.debug("Operation has no resource keys.");
			return;
		}

		if (operation.getResources() != null) {
			log.debug("Operation already has resources.");
			return;
		}

		operation.setResources(operation.getResourceKeys().stream().map(resourceKey -> {
			return resourceRepository.findByKey(resourceKey).orElseGet(() -> {
				log.debug("Did not find resource for key {}.", resourceKey);
				eventLogService.error(String.format("Unable to find Resource with key '%s'", resourceKey));
				return null;
			});
		}).filter(Objects::nonNull).collect(Collectors.toSet()));
	}
}
