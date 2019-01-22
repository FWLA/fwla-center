package de.ihrigb.fwla.fwlacenter.services.resource;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import de.ihrigb.fwla.fwlacenter.handling.api.Processor;
import de.ihrigb.fwla.fwlacenter.handling.core.ProcessorOrder;
import de.ihrigb.fwla.fwlacenter.persistence.model.PatternMode;
import de.ihrigb.fwla.fwlacenter.persistence.model.ResourceKeyPattern;
import de.ihrigb.fwla.fwlacenter.persistence.repository.ResourceKeyPatternRepository;
import de.ihrigb.fwla.fwlacenter.persistence.repository.ResourceRepository;
import de.ihrigb.fwla.fwlacenter.services.api.EventLogService;
import de.ihrigb.fwla.fwlacenter.persistence.model.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(ProcessorOrder.RESOURCE_PROCESSOR_ORDER)
@RequiredArgsConstructor
public class ResourceProcessor implements Processor {

	private final ResourceRepository resourceRepository;
	private final ResourceKeyPatternRepository resourceKeyPatternRepository;
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

		operation.setResources(operation.getResourceKeys().stream().filter(resourceKey -> {
			return isIncluded(resourceKey) && !isExcluded(resourceKey);
		}).map(resourceKey -> {
			return resourceRepository.findByKey(resourceKey).orElseGet(() -> {
				log.debug("Did not find resource for key {}.", resourceKey);
				eventLogService.error(String.format("Unable to find Resource with key '%s'", resourceKey));
				return null;
			});
		}).filter(Objects::nonNull).sorted((r1, r2) -> {
			return r1.getRadio().compareTo(r2.getRadio());
		}).collect(Collectors.toList()));
	}

	private boolean isIncluded(String resource) {
		Set<ResourceKeyPattern> includes = resourceKeyPatternRepository.findByMode(PatternMode.INCLUDE);
		if (includes.isEmpty()) {
			return true;
		}

		for (ResourceKeyPattern resourceKeyPattern : includes) {
			if (matches(resourceKeyPattern, resource)) {
				return true;
			}
		}

		return false;
	}

	private boolean isExcluded(String resource) {
		Set<ResourceKeyPattern> excludes = resourceKeyPatternRepository.findByMode(PatternMode.EXCLUDE);
		if (excludes.isEmpty()) {
			return false;
		}

		for (ResourceKeyPattern resourceKeyPattern : excludes) {
			if (matches(resourceKeyPattern, resource)) {
				return true;
			}
		}

		return false;
	}

	private boolean matches(ResourceKeyPattern resourceKeyPattern, String resource) {
		return resource.matches(resourceKeyPattern.getPattern());
	}
}
