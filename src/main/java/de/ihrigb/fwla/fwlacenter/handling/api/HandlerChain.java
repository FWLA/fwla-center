package de.ihrigb.fwla.fwlacenter.handling.api;

import de.ihrigb.fwla.fwlacenter.services.api.Operation;

public interface HandlerChain {
	void handle(Operation operation);
}
