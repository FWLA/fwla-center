package de.ihrigb.fwla.fwlacenter.services.eventlog;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;

import de.ihrigb.fwla.fwlacenter.persistence.model.EventLog;
import de.ihrigb.fwla.fwlacenter.persistence.model.EventLogType;
import de.ihrigb.fwla.fwlacenter.persistence.repository.EventLogRepository;

public class EventLogServiceImplTest {

	private EventLogServiceImpl testee;

	private EventLogRepository eventLogRepository;

	@Before
	public void setUp() {
		eventLogRepository = mock(EventLogRepository.class);
		testee = new EventLogServiceImpl(eventLogRepository);
	}

	@Test
	public void testInfo() throws Exception {

		Capture<EventLog> capture = Capture.newInstance();
		expect(eventLogRepository.save(capture(capture))).andAnswer(() -> {
			return capture.getValue();
		});

		replay(eventLogRepository);
		testee.info("message");

		EventLog eventLog = capture.getValue();
		assertEquals("message", eventLog.getMessage());
		assertNotNull(eventLog.getTime());
		assertSame(EventLogType.INFO, eventLog.getType());
		verify(eventLogRepository);
	}

	@Test
	public void testError() throws Exception {

		Capture<EventLog> capture = Capture.newInstance();
		expect(eventLogRepository.save(capture(capture))).andAnswer(() -> {
			return capture.getValue();
		});

		replay(eventLogRepository);
		testee.error("message");

		EventLog eventLog = capture.getValue();
		assertEquals("message", eventLog.getMessage());
		assertNotNull(eventLog.getTime());
		assertSame(EventLogType.ERROR, eventLog.getType());
		verify(eventLogRepository);
	}
}
