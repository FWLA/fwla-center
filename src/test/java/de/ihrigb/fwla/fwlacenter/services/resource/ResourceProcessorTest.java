package de.ihrigb.fwla.fwlacenter.services.resource;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Collections;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import de.ihrigb.fwla.fwlacenter.persistence.model.PatternMode;
import de.ihrigb.fwla.fwlacenter.persistence.model.Resource;
import de.ihrigb.fwla.fwlacenter.persistence.model.ResourceKeyPattern;
import de.ihrigb.fwla.fwlacenter.persistence.repository.ResourceKeyPatternRepository;
import de.ihrigb.fwla.fwlacenter.persistence.repository.ResourceRepository;
import de.ihrigb.fwla.fwlacenter.services.api.EventLogService;
import de.ihrigb.fwla.fwlacenter.services.api.Operation;

public class ResourceProcessorTest {

	private ResourceProcessor testee;

	private ResourceRepository resourceRepository;
	private ResourceKeyPatternRepository resourceKeyPatternRepository;
	private EventLogService eventLogService;

	@Before
	public void setUp() {
		resourceRepository = mock(ResourceRepository.class);
		resourceKeyPatternRepository = mock(ResourceKeyPatternRepository.class);
		eventLogService = mock(EventLogService.class);

		testee = new ResourceProcessor(resourceRepository, resourceKeyPatternRepository, eventLogService);
	}

	@Test
	public void testNullResourceKeys() {
		Operation operation = new Operation();
		testee.process(operation);
		assertNull(operation.getResources());
	}

	@Test
	public void testAlreadyHasResources() {

		Resource resource = new Resource();
		resource.setName("key");
		resource.setKey("key");

		Operation operation = new Operation();
		operation.setResourceKeys(Collections.singletonList("key"));
		operation.setResources(Collections.singleton(resource));

		testee.process(operation);
		assertEquals(Collections.singleton(resource), operation.getResources());
	}

	@Test
	public void testNoIncludesNoExcludes() {

		Operation operation = new Operation();
		operation.setResourceKeys(Collections.singletonList("key"));

		expect(resourceKeyPatternRepository.findByMode(PatternMode.INCLUDE)).andReturn(Collections.emptySet());
		expect(resourceKeyPatternRepository.findByMode(PatternMode.EXCLUDE)).andReturn(Collections.emptySet());

		Resource resource = new Resource();
		resource.setName("key");
		resource.setKey("key");
		expect(resourceRepository.findByKey("key")).andReturn(Optional.of(resource));

		replay(resourceRepository, resourceKeyPatternRepository, eventLogService);
		testee.process(operation);
		assertEquals(Collections.singleton(resource), operation.getResources());
		verify(resourceRepository, resourceKeyPatternRepository, eventLogService);
	}

	@Test
	public void testNotIncluded() {

		Operation operation = new Operation();
		operation.setResourceKeys(Collections.singletonList("key"));

		ResourceKeyPattern resourceKeyPattern = new ResourceKeyPattern();
		resourceKeyPattern.setPattern("any.*");
		expect(resourceKeyPatternRepository.findByMode(PatternMode.INCLUDE)).andReturn(Collections.singleton(resourceKeyPattern));

		replay(resourceRepository, resourceKeyPatternRepository, eventLogService);
		testee.process(operation);
		assertEquals(Collections.emptySet(), operation.getResources());
		verify(resourceRepository, resourceKeyPatternRepository, eventLogService);
	}

	@Test
	public void testNotIncludedButExcluded() {

		Operation operation = new Operation();
		operation.setResourceKeys(Collections.singletonList("key1"));

		ResourceKeyPattern include = new ResourceKeyPattern();
		include.setPattern("key.*");
		expect(resourceKeyPatternRepository.findByMode(PatternMode.INCLUDE)).andReturn(Collections.singleton(include));

		ResourceKeyPattern exclude = new ResourceKeyPattern();
		exclude.setPattern("key1.*");
		expect(resourceKeyPatternRepository.findByMode(PatternMode.EXCLUDE)).andReturn(Collections.singleton(exclude));

		replay(resourceRepository, resourceKeyPatternRepository, eventLogService);
		testee.process(operation);
		assertEquals(Collections.emptySet(), operation.getResources());
		verify(resourceRepository, resourceKeyPatternRepository, eventLogService);
	}

	@Test
	public void testNotFound() {

		Operation operation = new Operation();
		operation.setResourceKeys(Collections.singletonList("key2"));

		ResourceKeyPattern include = new ResourceKeyPattern();
		include.setPattern("key.*");
		expect(resourceKeyPatternRepository.findByMode(PatternMode.INCLUDE)).andReturn(Collections.singleton(include));

		ResourceKeyPattern exclude = new ResourceKeyPattern();
		exclude.setPattern("key1.*");
		expect(resourceKeyPatternRepository.findByMode(PatternMode.EXCLUDE)).andReturn(Collections.singleton(exclude));

		expect(resourceRepository.findByKey("key2")).andReturn(Optional.empty());

		eventLogService.error(anyString());
		expectLastCall();

		replay(resourceRepository, resourceKeyPatternRepository, eventLogService);
		testee.process(operation);
		assertEquals(Collections.emptySet(), operation.getResources());
		verify(resourceRepository, resourceKeyPatternRepository, eventLogService);
	}
}
