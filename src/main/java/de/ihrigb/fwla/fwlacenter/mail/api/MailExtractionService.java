package de.ihrigb.fwla.fwlacenter.mail.api;

import de.ihrigb.fwla.fwlacenter.mail.Email;
import de.ihrigb.fwla.fwlacenter.operation.Operation;

public interface MailExtractionService {
	Operation extract(Email email);
}
