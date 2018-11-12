package de.ihrigb.fwla.fwlacenter.handling.core;

import java.util.Set;

import org.springframework.stereotype.Component;

import de.ihrigb.fwla.fwlacenter.handling.api.Handler;
import de.ihrigb.fwla.fwlacenter.handling.api.OperationChain;
import de.ihrigb.fwla.fwlacenter.handling.api.Processor;
import de.ihrigb.fwla.fwlacenter.services.api.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OperationsChain implements OperationChain {

	private final Set<Processor> processors;
	private final Set<Handler> handlers;

	@Override
	public void put(Operation operation) {
		try {
			for (Processor processor : processors) {
				processor.process(operation);
			}
			for (Handler handler : handlers) {
				handler.handle(operation);
			}
		} catch (RuntimeException e) {
			log.error("Exception in operation chain.", e);
		}
	}
}
