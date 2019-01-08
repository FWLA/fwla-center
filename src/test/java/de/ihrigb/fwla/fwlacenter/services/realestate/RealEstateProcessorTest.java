package de.ihrigb.fwla.fwlacenter.services.realestate;

import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Collections;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import de.ihrigb.fwla.fwlacenter.persistence.model.RealEstate;
import de.ihrigb.fwla.fwlacenter.persistence.repository.RealEstateRepository;
import de.ihrigb.fwla.fwlacenter.services.api.EventLogService;
import de.ihrigb.fwla.fwlacenter.services.api.Operation;

public class RealEstateProcessorTest {

	private RealEstateProcessor testee;
	private RealEstateRepository realEstateRepository;
	private EventLogService eventLogService;

	@Before
	public void setUp() {
		realEstateRepository = mock(RealEstateRepository.class);
		eventLogService = mock(EventLogService.class);
		testee = new RealEstateProcessor(realEstateRepository, eventLogService);
	}

	@Test
	public void testProcessNoObject() throws Exception {
		Operation operation = new Operation();

		replay(realEstateRepository, eventLogService);
		testee.process(operation);

		assertNull(operation.getRealEstate());
		verify(realEstateRepository, eventLogService);
	}

	@Test
	public void testProcessRealEstatePreset() throws Exception {
		Operation operation = new Operation();
		RealEstate realEstate = new RealEstate();
		operation.setRealEstate(realEstate);

		replay(realEstateRepository, eventLogService);
		testee.process(operation);

		assertSame(realEstate, operation.getRealEstate());
		verify(realEstateRepository, eventLogService);
	}

	@Test
	public void testProcessFoundByName() throws Exception {
		Operation operation = new Operation();
		operation.setObject("objectkey");

		RealEstate realEstate = new RealEstate();
		expect(realEstateRepository.findOneByName("objectkey")).andReturn(Optional.of(realEstate));

		replay(realEstateRepository, eventLogService);
		testee.process(operation);

		assertSame(realEstate, operation.getRealEstate());
		verify(realEstateRepository, eventLogService);
	}

	@Test
	public void testProcessFoundByPattern() throws Exception {
		Operation operation = new Operation();
		operation.setObject("objectkey");

		expect(realEstateRepository.findOneByName("objectkey")).andReturn(Optional.empty());

		RealEstate realEstate = new RealEstate();
		realEstate.setPattern("object.*");
		expect(realEstateRepository.streamAll()).andReturn(Collections.singleton(realEstate).stream());

		replay(realEstateRepository, eventLogService);
		testee.process(operation);

		assertSame(realEstate, operation.getRealEstate());
		verify(realEstateRepository, eventLogService);
	}

	@Test
	public void testProcessNotFound() throws Exception {
		Operation operation = new Operation();
		operation.setObject("objectkey");

		expect(realEstateRepository.findOneByName("objectkey")).andReturn(Optional.empty());

		RealEstate realEstate = new RealEstate();
		realEstate.setPattern("otherobject.*");
		expect(realEstateRepository.streamAll()).andReturn(Collections.singleton(realEstate).stream());

		eventLogService.error(anyString(), anyString());
		expectLastCall();

		replay(realEstateRepository, eventLogService);
		testee.process(operation);

		assertNull(operation.getRealEstate());
		verify(realEstateRepository, eventLogService);
	}
}
