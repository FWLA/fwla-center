package de.ihrigb.fwla.fwlacenter.mail.parse;

import static org.junit.Assert.assertEquals;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import de.ihrigb.fwla.fwlacenter.persistence.model.RegexPattern;
import de.ihrigb.fwla.fwlacenter.persistence.repository.RegexPatternRepository;
import de.ihrigb.fwla.fwlacenter.services.api.Operation;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RegexTest {

	@Autowired
	private RegexPatternRepository repository;

	@Test
	public void testTimePattern() throws Exception {
		// Winter Time
		assertEquals("2019-01-01T02:40:49Z", parse("Zeiten:        01.01.2019 03:37:46     01.01.2019 03:40:49\r\n", Fields.TIME).getTime().toString());
		// Summer Time
		assertEquals("2019-08-01T01:40:49Z", parse("Zeiten:        01.08.2019 03:37:46     01.08.2019 03:40:49\r\n", Fields.TIME).getTime().toString());
	}

	@Test
	public void testCodePattern() throws Exception {
		assertEquals("F-2", parse("1234567890 / F-2 - , Musterstadt-Musterort, Musterstraße", Fields.CODE).getCode());
		assertEquals("H-PWASS", parse("1234567890 / H-PWASS - , Musterstadt-Musterort, Fluss - Ort > bis Musterstadt", Fields.CODE).getCode());
	}

	private Operation parse(String heystack, Fields field) {
		RegexPattern regexPattern = getPattern(field);
		Pattern pattern = Pattern.compile(regexPattern.getPattern());
		Matcher matcher = pattern.matcher(heystack);
		Operation operation = new Operation();

		field.getPopulator().accept(operation, matcher);

		return operation;
	}

	private RegexPattern getPattern(Fields field) {
		return repository.findById(field.getName()).orElseThrow(() -> {
			return new AssertionError("Did not find pattern for field.");
		});
	}
}
