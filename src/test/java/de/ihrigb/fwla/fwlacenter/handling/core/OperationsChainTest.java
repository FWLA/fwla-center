package de.ihrigb.fwla.fwlacenter.handling.core;

import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.ObjectProvider;

import de.ihrigb.fwla.fwlacenter.handling.api.Handler;
import de.ihrigb.fwla.fwlacenter.handling.api.Processor;
import de.ihrigb.fwla.fwlacenter.persistence.model.Operation;

public class OperationsChainTest {

	private OperationsChain testee;

	private ObjectProvider<Processor> objectProvider;
	private Processor processor;
	private Handler handler;

	@Before
	public void setUp() {
		objectProvider = mock(ObjectProvider.class);
		processor = mock(Processor.class);
		handler = mock(Handler.class);

		testee = new OperationsChain(objectProvider, Collections.singleton(handler));
	}

	@Test
	public void testPut() throws Exception {
		Operation operation = mock(Operation.class);
		expect(operation.getId()).andReturn("id");

		expect(objectProvider.orderedStream()).andReturn(Stream.of(processor));

		processor.process(operation);
		expectLastCall();

		handler.handle(operation);
		expectLastCall();

		replay(objectProvider, processor, handler, operation);
		testee.put(operation);
		verify(objectProvider, processor, handler, operation);
	}

	@Test
	public void testPutException() throws Exception {
		Operation operation = mock(Operation.class);
		expect(operation.getId()).andReturn("id");

		expect(objectProvider.orderedStream()).andReturn(Stream.of(processor));

		processor.process(operation);
		expectLastCall();

		handler.handle(operation);
		expectLastCall().andThrow(new RuntimeException());

		replay(objectProvider, processor, handler, operation);
		testee.put(operation);
		verify(objectProvider, processor, handler, operation);
	}
}
