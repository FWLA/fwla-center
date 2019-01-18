package de.ihrigb.fwla.fwlacenter.services.operation;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertSame;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import de.ihrigb.fwla.fwlacenter.persistence.model.Operation;
import de.ihrigb.fwla.fwlacenter.persistence.model.OperationKey;
import de.ihrigb.fwla.fwlacenter.persistence.repository.OperationKeyRepository;
import de.ihrigb.fwla.fwlacenter.services.api.EventLogService;

public class OperationKeyProcessorTest {

	private OperationKeyProcessor testee;

	private OperationKeyRepository operationKeyRepository;
	private EventLogService eventLogService;

	@Before
	public void setUp() {
		operationKeyRepository = mock(OperationKeyRepository.class);
		eventLogService = mock(EventLogService.class);
		testee = new OperationKeyProcessor(operationKeyRepository, eventLogService);
	}

	@Test
	public void testProcessNullCode() throws Exception {
		Operation operation = new Operation();

		replay(operationKeyRepository, eventLogService);
		testee.process(operation);
		verify(operationKeyRepository, eventLogService);
	}

	@Test
	public void testProcessOperationKeySet() throws Exception {
		Operation operation = new Operation();
		operation.setCode("F 1");

		OperationKey operationKey = new OperationKey();
		operation.setOperationKey(operationKey);

		replay(operationKeyRepository, eventLogService);
		testee.process(operation);
		verify(operationKeyRepository, eventLogService);
	}

	@Test
	public void testProcessCodeNotFound() throws Exception {
		Operation operation = new Operation();
		operation.setCode("F 1");

		expect(operationKeyRepository.findOneByCode("F 1")).andReturn(Optional.empty());

		eventLogService.error(anyString(), anyString());
		expectLastCall();

		replay(operationKeyRepository, eventLogService);
		testee.process(operation);
		verify(operationKeyRepository, eventLogService);
	}

	@Test
	public void testProcessCodeFound() throws Exception {
		Operation operation = new Operation();
		operation.setCode("F 1");

		OperationKey operationKey = new OperationKey();
		expect(operationKeyRepository.findOneByCode("F 1")).andReturn(Optional.of(operationKey));

		replay(operationKeyRepository, eventLogService);
		testee.process(operation);
		assertSame(operationKey, operation.getOperationKey());
		verify(operationKeyRepository, eventLogService);
	}
}
