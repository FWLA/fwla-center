package de.ihrigb.fwla.fwlacenter.mail.api;

import de.ihrigb.fwla.fwlacenter.mail.Email;
import de.ihrigb.fwla.fwlacenter.services.api.Operation;

public interface MailExtractionService {
	Operation extract(Email email);
}
