package de.ihrigb.fwla.fwlacenter.mail.parse;

import java.util.regex.Matcher;

import de.ihrigb.fwla.fwlacenter.persistence.model.Operation;
import de.ihrigb.fwla.fwlacenter.services.api.EventLogService;
import de.ihrigb.fwla.fwlacenter.utils.TriConsumer;

public interface Field {
	String getName();
	TriConsumer<Operation, Matcher, EventLogService> getPopulator();
}
