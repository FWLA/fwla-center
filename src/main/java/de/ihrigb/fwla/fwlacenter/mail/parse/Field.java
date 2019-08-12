package de.ihrigb.fwla.fwlacenter.mail.parse;

import java.util.function.BiConsumer;
import java.util.regex.Matcher;

import de.ihrigb.fwla.fwlacenter.persistence.model.Operation;

public interface Field {
	String getName();
	BiConsumer<Operation, Matcher> getPopulator();
}
