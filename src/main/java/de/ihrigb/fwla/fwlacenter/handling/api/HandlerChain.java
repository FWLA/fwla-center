package de.ihrigb.fwla.fwlacenter.handling.api;

import de.ihrigb.fwla.fwlacenter.operation.Operation;

public interface HandlerChain {
	void handle(Operation operation);
}
