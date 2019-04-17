package de.ihrigb.fwla.fwlacenter.services.display;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import de.ihrigb.fwla.fwlacenter.persistence.repository.DisplayEventRepository;

public class DisplayEventCleanupTest {

	private DisplayEventCleanup testee;

	private DisplayEventRepository displayEventRepository;

	@Before
	public void setUp() throws Exception {
		displayEventRepository = mock(DisplayEventRepository.class);
		testee = new DisplayEventCleanup(displayEventRepository);
	}

	@Test
	public void testDeleteOutdatedDisplayEvents() throws Exception {
		testee.deleteOutdatedDisplayEvents();
		verify(displayEventRepository, times(1)).deleteOutdatedDisplayEvents();
	}
}
