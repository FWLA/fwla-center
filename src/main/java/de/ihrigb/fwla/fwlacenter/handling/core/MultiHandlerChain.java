package de.ihrigb.fwla.fwlacenter.handling.core;

import java.util.Set;

import org.springframework.stereotype.Component;

import de.ihrigb.fwla.fwlacenter.handling.api.Handler;
import de.ihrigb.fwla.fwlacenter.handling.api.HandlerChain;
import de.ihrigb.fwla.fwlacenter.services.api.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MultiHandlerChain implements HandlerChain {

	private final Set<Handler> handlers;

	@Override
	public void handle(Operation operation) {
		for (Handler handler : handlers) {
			try {
				handler.handle(operation);
			} catch (RuntimeException e) {
				log.error("Exception in handler " + handler.getClass().getName() + ".", e);
			}
		}
	}
}
