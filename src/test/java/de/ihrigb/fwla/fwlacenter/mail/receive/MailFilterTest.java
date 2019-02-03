package de.ihrigb.fwla.fwlacenter.mail.receive;

import static org.junit.Assert.assertSame;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import de.ihrigb.fwla.fwlacenter.mail.receive.MailFilter.FilterResult;

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
		assertSame(FilterResult.REJECTED, testee.filter("mail@domain.de"));
	}

	@Test
	public void testHot() {
		properties.setWhitelistHot(Collections.singleton("SenderA@domain.de"));

		assertSame(FilterResult.REJECTED, testee.filter("SenderB@domain.de"));
		assertSame(FilterResult.HOT, testee.filter("SenderA@domain.de"));
	}

	@Test
	public void testTraining() {
		properties.setWhitelistHot(Collections.singleton("SenderA@domain.de"));
		properties.setWhitelistTraining(Collections.singleton("SenderC@domain.de"));

		assertSame(FilterResult.REJECTED, testee.filter("SenderB@domain.de"));
		assertSame(FilterResult.HOT, testee.filter("SenderA@domain.de"));
		assertSame(FilterResult.TRAINING, testee.filter("SenderC@domain.de"));
	}
}
