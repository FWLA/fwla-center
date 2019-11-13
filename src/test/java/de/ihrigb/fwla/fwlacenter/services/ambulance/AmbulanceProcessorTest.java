package de.ihrigb.fwla.fwlacenter.services.ambulance;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.ihrigb.fwla.fwlacenter.persistence.model.AmbulancePattern;
import de.ihrigb.fwla.fwlacenter.persistence.model.Operation;
import de.ihrigb.fwla.fwlacenter.persistence.model.PatternMode;
import de.ihrigb.fwla.fwlacenter.persistence.repository.AmbulancePatternRepository;

public class AmbulanceProcessorTest {

	private AmbulanceProcessor testee;
	private AmbulancePatternRepository ambulancePatternRepository;

	@Before
	public void setUp() throws Exception {
		this.ambulancePatternRepository = Mockito.mock(AmbulancePatternRepository.class);
		this.testee = new AmbulanceProcessor(ambulancePatternRepository);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullOperation() throws Exception {
		this.testee.process(null);
	}

	@Test
	public void testNoResourceKeys() throws Exception {
		Operation operation = new Operation();
		this.testee.process(operation);
		Assert.assertFalse(operation.isAmbulanceCalled());
	}

	@Test
	public void testNoAmbulancePatterns() throws Exception {
		Operation operation = new Operation();
		operation.setResourceKeys(Collections.singletonList("RKB 13/83-1"));

		Mockito.when(ambulancePatternRepository.findByMode(PatternMode.INCLUDE)).thenReturn(Collections.emptySet());
		Mockito.when(ambulancePatternRepository.findByMode(PatternMode.EXCLUDE)).thenReturn(Collections.emptySet());

		this.testee.process(operation);
		Assert.assertFalse(operation.isAmbulanceCalled());
	}

	@Test
	public void testIncludeOnlyIncludePatterns() throws Exception {
		Operation operation = new Operation();
		operation.setResourceKeys(Collections.singletonList("RKB 13/83-1"));

		AmbulancePattern includePattern = new AmbulancePattern();
		includePattern.setMode(PatternMode.INCLUDE);
		includePattern.setPattern("RKB.*");

		Mockito.when(ambulancePatternRepository.findByMode(PatternMode.INCLUDE))
				.thenReturn(Collections.singleton(includePattern));
		Mockito.when(ambulancePatternRepository.findByMode(PatternMode.EXCLUDE)).thenReturn(Collections.emptySet());

		this.testee.process(operation);
		Assert.assertTrue(operation.isAmbulanceCalled());
	}

	@Test
	public void testIncludeOnlyExcludePatterns() throws Exception {
		Operation operation = new Operation();
		operation.setResourceKeys(Collections.singletonList("RKB 13/83-1"));

		AmbulancePattern excludePattern = new AmbulancePattern();
		excludePattern.setMode(PatternMode.INCLUDE);
		excludePattern.setPattern("RKB.*");

		Mockito.when(ambulancePatternRepository.findByMode(PatternMode.INCLUDE)).thenReturn(Collections.emptySet());
		Mockito.when(ambulancePatternRepository.findByMode(PatternMode.EXCLUDE))
				.thenReturn(Collections.singleton(excludePattern));

		this.testee.process(operation);
		Assert.assertFalse(operation.isAmbulanceCalled());
	}

	@Test
	public void testIncludedButExcluded() throws Exception {
		Operation operation = new Operation();
		operation.setResourceKeys(Collections.singletonList("RKB 13/83-1"));

		AmbulancePattern includePattern = new AmbulancePattern();
		includePattern.setMode(PatternMode.INCLUDE);
		includePattern.setPattern("RKB.*");

		AmbulancePattern excludePattern = new AmbulancePattern();
		excludePattern.setMode(PatternMode.INCLUDE);
		excludePattern.setPattern("RKB.*83.*");

		Mockito.when(ambulancePatternRepository.findByMode(PatternMode.INCLUDE))
				.thenReturn(Collections.singleton(includePattern));
		Mockito.when(ambulancePatternRepository.findByMode(PatternMode.EXCLUDE))
				.thenReturn(Collections.singleton(excludePattern));

		this.testee.process(operation);
		Assert.assertFalse(operation.isAmbulanceCalled());
	}

	@Test
	public void testIncluded() throws Exception {
		Operation operation = new Operation();
		operation.setResourceKeys(Collections.singletonList("RKB 13/83-1"));

		AmbulancePattern includePattern = new AmbulancePattern();
		includePattern.setMode(PatternMode.INCLUDE);
		includePattern.setPattern("RKB.*");

		AmbulancePattern excludePattern = new AmbulancePattern();
		excludePattern.setMode(PatternMode.INCLUDE);
		excludePattern.setPattern("AKB.*");

		Mockito.when(ambulancePatternRepository.findByMode(PatternMode.INCLUDE))
				.thenReturn(Collections.singleton(includePattern));
		Mockito.when(ambulancePatternRepository.findByMode(PatternMode.EXCLUDE))
				.thenReturn(Collections.singleton(excludePattern));

		this.testee.process(operation);
		Assert.assertTrue(operation.isAmbulanceCalled());
	}
}
