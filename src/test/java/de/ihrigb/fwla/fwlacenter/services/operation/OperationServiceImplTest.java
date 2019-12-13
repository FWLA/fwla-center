package de.ihrigb.fwla.fwlacenter.services.operation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

import de.ihrigb.fwla.fwlacenter.persistence.model.Operation;
import de.ihrigb.fwla.fwlacenter.persistence.model.Resource;
import de.ihrigb.fwla.fwlacenter.persistence.model.Station;
import de.ihrigb.fwla.fwlacenter.persistence.repository.OperationRepository;

public class OperationServiceImplTest {

	@Rule
	public MockitoRule mockito = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

	private OperationServiceImpl testee;
	private OperationRepository operationRepository;
	private OperationProperties properties;

	@Before
	public void setUp() {
		operationRepository = mock(OperationRepository.class);
		properties = new OperationProperties();
		testee = new OperationServiceImpl(operationRepository, properties);
	}

	@Test
	public void testInitial() throws Exception {
		Station s = station("id", "name");

		assertTrue(testee.getOperations().isEmpty());
		assertFalse(testee.getActiveOperation(s).isPresent());
		assertTrue(testee.getCurrentOperations(s).isEmpty());
		assertFalse(testee.hasCurrentOperations(s));
	}

	@Test
	public void testSimpleAddAndClose() throws Exception {
		Station s = station("id", "name");
		Resource r = resource("id", "name", s);
		Operation o = operation("id", false, r);

		when(operationRepository.save(o)).thenReturn(o);

		testee.addOperation(o);

		assertEquals(1, testee.getOperations().size());
		assertSame(o, testee.getActiveOperation(s).get());
		assertEquals(1, testee.getCurrentOperations(s).size());
		assertTrue(testee.hasCurrentOperations(s));

		testee.closeOperation("id");

		assertEquals(0, testee.getOperations().size());
		assertFalse(testee.getActiveOperation(s).isPresent());
		assertTrue(testee.getCurrentOperations(s).isEmpty());
		assertFalse(testee.hasCurrentOperations(s));
	}

	@Test
	public void testTwoFiFo() throws Exception {
		Station s = station("id", "name");
		Resource r = resource("id", "name", s);
		Operation o1 = operation("id1", false, r);

		when(operationRepository.save(o1)).thenReturn(o1);

		testee.addOperation(o1);

		assertEquals(1, testee.getOperations().size());
		assertSame(o1, testee.getActiveOperation(s).get());
		assertEquals(1, testee.getCurrentOperations(s).size());
		assertTrue(testee.hasCurrentOperations(s));

		Operation o2 = operation("id2", false, r);

		when(operationRepository.save(o2)).thenReturn(o2);

		testee.addOperation(o2);

		assertEquals(2, testee.getOperations().size());
		assertSame(o2, testee.getActiveOperation(s).get());
		assertEquals(2, testee.getCurrentOperations(s).size());
		assertTrue(testee.hasCurrentOperations(s));

		testee.closeOperation("id1");

		assertEquals(1, testee.getOperations().size());
		assertSame(o2, testee.getActiveOperation(s).get());
		assertEquals(1, testee.getCurrentOperations(s).size());
		assertTrue(testee.hasCurrentOperations(s));

		testee.closeOperation("id2");

		assertEquals(0, testee.getOperations().size());
		assertFalse(testee.getActiveOperation(s).isPresent());
		assertTrue(testee.getCurrentOperations(s).isEmpty());
		assertFalse(testee.hasCurrentOperations(s));
	}

	@Test
	public void testTwoFiLo() throws Exception {
		Station s = station("id", "name");
		Resource r = resource("id", "name", s);
		Operation o1 = operation("id1", false, r);

		when(operationRepository.save(o1)).thenReturn(o1);

		testee.addOperation(o1);

		assertEquals(1, testee.getOperations().size());
		assertSame(o1, testee.getActiveOperation(s).get());
		assertEquals(1, testee.getCurrentOperations(s).size());
		assertTrue(testee.hasCurrentOperations(s));

		Operation o2 = operation("id2", false, r);

		when(operationRepository.save(o2)).thenReturn(o2);


		testee.addOperation(o2);

		assertEquals(2, testee.getOperations().size());
		assertSame(o2, testee.getActiveOperation(s).get());
		assertEquals(2, testee.getCurrentOperations(s).size());
		assertTrue(testee.hasCurrentOperations(s));

		testee.closeOperation("id2");

		assertEquals(1, testee.getOperations().size());
		assertSame(o1, testee.getActiveOperation(s).get());
		assertEquals(1, testee.getCurrentOperations(s).size());
		assertTrue(testee.hasCurrentOperations(s));

		testee.closeOperation("id1");

		assertEquals(0, testee.getOperations().size());
		assertFalse(testee.getActiveOperation(s).isPresent());
		assertTrue(testee.getCurrentOperations(s).isEmpty());
		assertFalse(testee.hasCurrentOperations(s));
	}

	@Test
	public void testOperationsOfDistinctStations() throws Exception {
		Station s1 = station("id1", "name");
		Resource r1 = resource("id1", "name", s1);
		Operation o1 = operation("id1", false, r1);

		Station s2 = station("id2", "name");
		Resource r2 = resource("id2", "name", s2);
		Operation o2 = operation("id2", false, r2);

		when(operationRepository.save(o1)).thenReturn(o1);
		when(operationRepository.save(o2)).thenReturn(o2);

		testee.addOperation(o1);

		assertEquals(1, testee.getOperations().size());
		assertSame(o1, testee.getActiveOperation(s1).get());
		assertEquals(1, testee.getCurrentOperations(s1).size());
		assertTrue(testee.hasCurrentOperations(s1));
		assertFalse(testee.getActiveOperation(s2).isPresent());
		assertTrue(testee.getCurrentOperations(s2).isEmpty());
		assertFalse(testee.hasCurrentOperations(s2));


		testee.addOperation(o2);

		assertEquals(2, testee.getOperations().size());
		assertSame(o1, testee.getActiveOperation(s1).get());
		assertEquals(1, testee.getCurrentOperations(s1).size());
		assertTrue(testee.hasCurrentOperations(s1));
		assertSame(o2, testee.getActiveOperation(s2).get());
		assertEquals(1, testee.getCurrentOperations(s2).size());
		assertTrue(testee.hasCurrentOperations(s2));

		testee.closeOperation("id2");

		assertEquals(1, testee.getOperations().size());
		assertSame(o1, testee.getActiveOperation(s1).get());
		assertEquals(1, testee.getCurrentOperations(s1).size());
		assertTrue(testee.hasCurrentOperations(s1));
		assertFalse(testee.getActiveOperation(s2).isPresent());
		assertTrue(testee.getCurrentOperations(s2).isEmpty());
		assertFalse(testee.hasCurrentOperations(s2));

		testee.closeOperation("id1");

		assertEquals(0, testee.getOperations().size());
		assertFalse(testee.getActiveOperation(s1).isPresent());
		assertTrue(testee.getCurrentOperations(s1).isEmpty());
		assertFalse(testee.hasCurrentOperations(s1));
		assertFalse(testee.getActiveOperation(s2).isPresent());
		assertTrue(testee.getCurrentOperations(s2).isEmpty());
		assertFalse(testee.hasCurrentOperations(s2));
	}

	@Test
	public void testOperationsOfMixedStations() throws Exception {
		Station s1 = station("id1", "name");
		Resource r1 = resource("id1", "name", s1);
		Operation o1 = operation("id1", false, r1);

		Station s2 = station("id2", "name");
		Resource r2 = resource("id2", "name", s2);
		Operation o2 = operation("id2", false, r1, r2);

		when(operationRepository.save(o1)).thenReturn(o1);
		when(operationRepository.save(o2)).thenReturn(o2);

		testee.addOperation(o1);

		assertEquals(1, testee.getOperations().size());
		assertSame(o1, testee.getActiveOperation(s1).get());
		assertEquals(1, testee.getCurrentOperations(s1).size());
		assertTrue(testee.hasCurrentOperations(s1));
		assertFalse(testee.getActiveOperation(s2).isPresent());
		assertTrue(testee.getCurrentOperations(s2).isEmpty());
		assertFalse(testee.hasCurrentOperations(s2));


		testee.addOperation(o2);

		assertEquals(2, testee.getOperations().size());
		assertSame(o2, testee.getActiveOperation(s1).get());
		assertEquals(2, testee.getCurrentOperations(s1).size());
		assertTrue(testee.hasCurrentOperations(s1));
		assertSame(o2, testee.getActiveOperation(s2).get());
		assertEquals(1, testee.getCurrentOperations(s2).size());
		assertTrue(testee.hasCurrentOperations(s2));

		testee.closeOperation("id2");

		assertEquals(1, testee.getOperations().size());
		assertSame(o1, testee.getActiveOperation(s1).get());
		assertEquals(1, testee.getCurrentOperations(s1).size());
		assertTrue(testee.hasCurrentOperations(s1));
		assertFalse(testee.getActiveOperation(s2).isPresent());
		assertTrue(testee.getCurrentOperations(s2).isEmpty());
		assertFalse(testee.hasCurrentOperations(s2));

		testee.closeOperation("id1");

		assertEquals(0, testee.getOperations().size());
		assertFalse(testee.getActiveOperation(s1).isPresent());
		assertTrue(testee.getCurrentOperations(s1).isEmpty());
		assertFalse(testee.hasCurrentOperations(s1));
		assertFalse(testee.getActiveOperation(s2).isPresent());
		assertTrue(testee.getCurrentOperations(s2).isEmpty());
		assertFalse(testee.hasCurrentOperations(s2));
	}

	@Test
	public void testTimeout() throws Exception {
		Station s = station("id", "name");
		Resource r = resource("id", "name", s);
		Operation o = operation("id", false, r);
		o.setCreated(Instant.ofEpochMilli(System.currentTimeMillis() - (21 * 60 * 1000)));

		when(operationRepository.save(o)).thenReturn(o);

		testee.addOperation(o);

		assertEquals(1, testee.getOperations().size());
		assertSame(o, testee.getActiveOperation(s).get());
		assertEquals(1, testee.getCurrentOperations(s).size());
		assertTrue(testee.hasCurrentOperations(s));

		testee.timeoutOperations();

		assertEquals(0, testee.getOperations().size());
		assertFalse(testee.getActiveOperation(s).isPresent());
		assertTrue(testee.getCurrentOperations(s).isEmpty());
		assertFalse(testee.hasCurrentOperations(s));
	}

	private Operation operation(String id, boolean isTraining, Resource... resources) {
		Operation o = new Operation();
		o.setId(id);
		o.setTraining(isTraining);
		o.setResources(Arrays.asList(resources));
		return o;
	}

	private Resource resource(String id, String name, Station station) {
		Resource r = new Resource();
		r.setId(id);
		r.setName(name);
		r.setStation(station);
		return r;
	}

	private Station station(String id, String name) {
		Station s = new Station();
		s.setId(id);
		s.setName(name);
		return s;
	}
}
