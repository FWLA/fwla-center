package de.ihrigb.fwla.fwlacenter.handling.core;

import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import de.ihrigb.fwla.fwlacenter.handling.api.Handler;
import de.ihrigb.fwla.fwlacenter.handling.api.Processor;
import de.ihrigb.fwla.fwlacenter.services.api.Operation;

public class OperationsChainTest {

	private OperationsChain testee;

	private Processor processor;
	private Handler handler;

	@Before
	public void setUp() {
		processor = mock(Processor.class);
		handler = mock(Handler.class);

		testee = new OperationsChain(Collections.singleton(processor), Collections.singleton(handler));
	}

	@Test
	public void testPut() throws Exception {
		Operation operation = mock(Operation.class);
		expect(operation.getId()).andReturn("id");

		processor.process(operation);
		expectLastCall();

		handler.handle(operation);
		expectLastCall();

		replay(processor, handler, operation);
		testee.put(operation);
		verify(processor, handler, operation);
	}

	@Test
	public void testPutException() throws Exception {
		Operation operation = mock(Operation.class);
		expect(operation.getId()).andReturn("id");

		processor.process(operation);
		expectLastCall();

		handler.handle(operation);
		expectLastCall().andThrow(new RuntimeException());

		replay(processor, handler, operation);
		testee.put(operation);
		verify(processor, handler, operation);
	}
}
