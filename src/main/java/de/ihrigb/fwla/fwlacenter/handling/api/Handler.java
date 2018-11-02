package de.ihrigb.fwla.fwlacenter.handling.api;

import java.util.Collections;
import java.util.Set;

import de.ihrigb.fwla.fwlacenter.services.api.Operation;

public interface Handler {
	void handle(Operation operation);

	default Set<Class<? extends Handler>> executeBefore() {
		return Collections.emptySet();
	}

	default Set<Class<? extends Handler>> executeAfter() {
		return Collections.emptySet();
	}
}
