package de.ihrigb.fwla.fwlacenter.handling.api;

import de.ihrigb.fwla.fwlacenter.persistence.model.Operation;

public interface Handler {
	void handle(Operation operation);
}
