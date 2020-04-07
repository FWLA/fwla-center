package de.ihrigb.fwla.fwlacenter.mail.api;

import de.ihrigb.fwla.fwlacenter.persistence.model.Operation;
import de.ihrigb.fwla.mail.Email;

public interface MailExtractionService {
	Operation extract(Email<String> email);
}
