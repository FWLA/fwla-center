package de.ihrigb.fwla.fwlacenter.services.operation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

import de.ihrigb.fwla.fwlacenter.services.api.Operation;

public class OperationServiceImplTest {

	@Rule
	public MockitoRule mockito = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

	private OperationServiceImpl testee;

	@Before
	public void setUp() {
		testee = new OperationServiceImpl();
	}

	@Test
	public void testInitial() throws Exception {
		assertFalse(testee.get("id").isPresent());
		assertFalse(testee.getActiveOperation().isPresent());
		assertTrue(testee.getCurrentOperations().isEmpty());
		assertFalse(testee.hasActiveOperation());
		assertFalse(testee.hasCurrentOperations());
	}

	@Test
	public void testSimpleAddAndClose() throws Exception {
		Operation operation = mock(Operation.class);

		when(operation.isTraining()).thenReturn(false);
		when(operation.getId()).thenReturn("id");

		testee.addOperation(operation);

		assertSame(operation, testee.get("id").get());
		assertSame(operation, testee.getActiveOperation().get());
		assertEquals(1, testee.getCurrentOperations().size());
		assertTrue(testee.hasActiveOperation());
		assertTrue(testee.hasCurrentOperations());

		testee.closeOperation("id");

		assertTrue(testee.get("id").isPresent());
		assertFalse(testee.getActiveOperation().isPresent());
		assertTrue(testee.getCurrentOperations().isEmpty());
		assertFalse(testee.hasActiveOperation());
		assertFalse(testee.hasCurrentOperations());
	}

	@Test
	public void testTwoFiFo() throws Exception {
		Operation operation1 = mock(Operation.class);

		when(operation1.isTraining()).thenReturn(false);
		when(operation1.getId()).thenReturn("id1");

		testee.addOperation(operation1);

		assertSame(operation1, testee.get("id1").get());
		assertSame(operation1, testee.getActiveOperation().get());
		assertEquals(1, testee.getCurrentOperations().size());
		assertTrue(testee.hasActiveOperation());
		assertTrue(testee.hasCurrentOperations());

		Operation operation2 = mock(Operation.class);

		when(operation2.isTraining()).thenReturn(false);
		when(operation2.getId()).thenReturn("id2");

		testee.addOperation(operation2);

		assertSame(operation2, testee.get("id2").get());
		assertSame(operation1, testee.getActiveOperation().get());
		assertEquals(2, testee.getCurrentOperations().size());
		assertTrue(testee.hasActiveOperation());
		assertTrue(testee.hasCurrentOperations());

		testee.closeOperation("id1");

		assertTrue(testee.get("id1").isPresent());
		assertSame(operation2, testee.getActiveOperation().get());
		assertEquals(1, testee.getCurrentOperations().size());
		assertTrue(testee.hasActiveOperation());
		assertTrue(testee.hasCurrentOperations());

		testee.closeOperation("id2");

		assertTrue(testee.get("id2").isPresent());
		assertFalse(testee.getActiveOperation().isPresent());
		assertTrue(testee.getCurrentOperations().isEmpty());
		assertFalse(testee.hasActiveOperation());
		assertFalse(testee.hasCurrentOperations());
	}

	@Test
	public void testTwoFiLo() throws Exception {
		Operation operation1 = mock(Operation.class);

		when(operation1.isTraining()).thenReturn(false);
		when(operation1.getId()).thenReturn("id1");

		testee.addOperation(operation1);

		assertSame(operation1, testee.get("id1").get());
		assertSame(operation1, testee.getActiveOperation().get());
		assertEquals(1, testee.getCurrentOperations().size());
		assertTrue(testee.hasActiveOperation());
		assertTrue(testee.hasCurrentOperations());

		Operation operation2 = mock(Operation.class);

		when(operation2.isTraining()).thenReturn(false);
		when(operation2.getId()).thenReturn("id2");

		testee.addOperation(operation2);

		assertSame(operation2, testee.get("id2").get());
		assertSame(operation1, testee.getActiveOperation().get());
		assertEquals(2, testee.getCurrentOperations().size());
		assertTrue(testee.hasActiveOperation());
		assertTrue(testee.hasCurrentOperations());

		testee.closeOperation("id2");

		assertTrue(testee.get("id2").isPresent());
		assertSame(operation1, testee.getActiveOperation().get());
		assertEquals(1, testee.getCurrentOperations().size());
		assertTrue(testee.hasActiveOperation());
		assertTrue(testee.hasCurrentOperations());

		testee.closeOperation("id1");

		assertTrue(testee.get("id1").isPresent());
		assertFalse(testee.getActiveOperation().isPresent());
		assertTrue(testee.getCurrentOperations().isEmpty());
		assertFalse(testee.hasActiveOperation());
		assertFalse(testee.hasCurrentOperations());
	}

	@Test
	public void testTimeout() throws Exception {
		Operation operation = mock(Operation.class);

		when(operation.isTraining()).thenReturn(false);
		when(operation.getId()).thenReturn("id");
		when(operation.getCreated()).thenReturn(Instant.ofEpochMilli(System.currentTimeMillis() - (16 * 60 * 1000)));

		testee.addOperation(operation);

		assertSame(operation, testee.get("id").get());
		assertSame(operation, testee.getActiveOperation().get());
		assertEquals(1, testee.getCurrentOperations().size());
		assertTrue(testee.hasActiveOperation());
		assertTrue(testee.hasCurrentOperations());

		testee.timeoutOperations();

		assertTrue(testee.get("id").isPresent());
		assertFalse(testee.getActiveOperation().isPresent());
		assertTrue(testee.getCurrentOperations().isEmpty());
		assertFalse(testee.hasActiveOperation());
		assertFalse(testee.hasCurrentOperations());
	}
}
