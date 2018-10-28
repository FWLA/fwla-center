package de.ihrigb.fwla.fwlacenter.mail.parse;

import java.util.function.BiConsumer;

import de.ihrigb.fwla.fwlacenter.operation.Operation;

public interface Field {
	String getName();
	BiConsumer<Operation, String> getPopulator();
}
