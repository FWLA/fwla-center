package de.ihrigb.fwla.fwlacenter.mail.parse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import de.ihrigb.fwla.fwlacenter.mail.Email;
import de.ihrigb.fwla.fwlacenter.mail.api.MailExtractionService;
import de.ihrigb.fwla.fwlacenter.operation.Operation;
import de.ihrigb.fwla.fwlacenter.persistence.model.RegexPattern;
import de.ihrigb.fwla.fwlacenter.persistence.repository.RegexPatternRepository;
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
				field.getPopulator().accept(operation, trim(evaluateRegexPattern(pattern, email)));
			});
		}

		return operation;
	}

	private String evaluateRegexPattern(RegexPattern regexPattern, Email email) {
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
		return evaluateRegexPattern(pattern, text);
	}

	private String evaluateRegexPattern(Pattern pattern, String text) {
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			if (matcher.groupCount() == 0) {
				return matcher.group();
			}
			return matcher.group(1);
		}
		return null;
	}

	private String trim(String string) {
		if (string == null) {
			return null;
		}

		String trimmed = string.trim();
		if ("".equals(trimmed)) {
			return null;
		}
		return trimmed;
	}
}
