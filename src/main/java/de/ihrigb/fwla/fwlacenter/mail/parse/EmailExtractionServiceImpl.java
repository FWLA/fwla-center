package de.ihrigb.fwla.fwlacenter.mail.parse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import de.ihrigb.fwla.fwlacenter.mail.Email;
import de.ihrigb.fwla.fwlacenter.mail.api.MailExtractionService;
import de.ihrigb.fwla.fwlacenter.persistence.model.RegexPattern;
import de.ihrigb.fwla.fwlacenter.persistence.repository.RegexPatternRepository;
import de.ihrigb.fwla.fwlacenter.services.api.Operation;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailExtractionServiceImpl implements MailExtractionService {

	private final RegexPatternRepository patternRepository;

	@Override
	public Operation extract(Email email) {
		Assert.notNull(email, "Email must not be null.");

		Operation operation = new Operation();

		for (Field field : Fields.values()) {
			patternRepository.findById(field.getName()).ifPresent(pattern -> {
				field.getPopulator().accept(operation, getMatcher(pattern, email));
			});
		}

		return operation;
	}

	private Matcher getMatcher(RegexPattern regexPattern, Email email) {
		String text;
		switch (regexPattern.getSource()) {
		case SUBJECT:
			text = email.getSubject();
			break;
		case BODY:
		default:
			text = email.getBody();
			break;
		}

		Pattern pattern = Pattern.compile(regexPattern.getPattern());
		return pattern.matcher(text);
	}
}
