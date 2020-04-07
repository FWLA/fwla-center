package de.ihrigb.fwla.fwlacenter.mail.receive;

import static org.junit.Assert.assertSame;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import de.ihrigb.fwla.fwlacenter.mail.receive.MailFilter.InternalFilterResult;

public class MailFilterTest {

	private MailFilter testee;
	private ReceivingProperties properties;

	@Before
	public void setUp() {
		properties = new ReceivingProperties();
		testee = new MailFilter(properties);
	}

	@Test
	public void testEmptyHotEmptyTraining() {
		assertSame(InternalFilterResult.REJECTED, testee.filterInternal("mail@domain.de"));
	}

	@Test
	public void testHot() {
		properties.setWhitelistHot(Collections.singleton("SenderA@domain.de"));

		assertSame(InternalFilterResult.REJECTED, testee.filterInternal("SenderB@domain.de"));
		assertSame(InternalFilterResult.HOT, testee.filterInternal("SenderA@domain.de"));
	}

	@Test
	public void testTraining() {
		properties.setWhitelistHot(Collections.singleton("SenderA@domain.de"));
		properties.setWhitelistTraining(Collections.singleton("SenderC@domain.de"));

		assertSame(InternalFilterResult.REJECTED, testee.filterInternal("SenderB@domain.de"));
		assertSame(InternalFilterResult.HOT, testee.filterInternal("SenderA@domain.de"));
		assertSame(InternalFilterResult.TRAINING, testee.filterInternal("SenderC@domain.de"));
	}
}
