package de.ihrigb.fwla.fwlacenter.services.ambulance;

import java.util.Set;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import de.ihrigb.fwla.fwlacenter.handling.api.Processor;
import de.ihrigb.fwla.fwlacenter.handling.core.ProcessorOrder;
import de.ihrigb.fwla.fwlacenter.persistence.model.AmbulancePattern;
import de.ihrigb.fwla.fwlacenter.persistence.model.PatternMode;
import de.ihrigb.fwla.fwlacenter.persistence.repository.AmbulancePatternRepository;
import de.ihrigb.fwla.fwlacenter.persistence.model.Operation;
import lombok.RequiredArgsConstructor;

@Component
@Order(ProcessorOrder.AMBULANCE_PROCESSOR_ORDER)
@RequiredArgsConstructor
public class AmbulanceProcessor implements Processor {

	private final AmbulancePatternRepository ambulancePatternRepository;

	@Override
	public void process(Operation operation) {
		Assert.notNull(operation, "Operation must not be null.");

		if (operation.getResourceKeys() == null) {
			return;
		}

		for (String resourceKey : operation.getResourceKeys()) {
			if (isIncluded(resourceKey) && !isExcluded(resourceKey)) {
				operation.setAmbulanceCalled(true);
				return;
			}
		}
	}

	private boolean isIncluded(String resource) {
		Set<AmbulancePattern> includes = ambulancePatternRepository.findByMode(PatternMode.INCLUDE);
		if (includes.isEmpty()) {
			return false;
		}

		for (AmbulancePattern ambulancePattern : includes) {
			if (matches(ambulancePattern, resource)) {
				return true;
			}
		}

		return false;
	}

	private boolean isExcluded(String resource) {
		Set<AmbulancePattern> excludes = ambulancePatternRepository.findByMode(PatternMode.EXCLUDE);
		if (excludes.isEmpty()) {
			return false;
		}

		for (AmbulancePattern ambulancePattern : excludes) {
			if (matches(ambulancePattern, resource)) {
				return true;
			}
		}

		return false;
	}

	private boolean matches(AmbulancePattern ambulancePattern, String resource) {
		return resource.matches(ambulancePattern.getPattern());
	}
}
