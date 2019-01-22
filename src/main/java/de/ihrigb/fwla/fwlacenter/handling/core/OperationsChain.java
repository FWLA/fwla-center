package de.ihrigb.fwla.fwlacenter.handling.core;

import java.util.Set;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import de.ihrigb.fwla.fwlacenter.handling.api.Handler;
import de.ihrigb.fwla.fwlacenter.handling.api.OperationChain;
import de.ihrigb.fwla.fwlacenter.handling.api.Processor;
import de.ihrigb.fwla.fwlacenter.persistence.model.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OperationsChain implements OperationChain {

	private final ObjectProvider<Processor> processors;
	private final Set<Handler> handlers;

	@Override
	public void put(Operation operation) {
		log.debug("New operation {} put into chain.", operation.getId());
		try {
			processors.orderedStream().forEach(processor -> {
				log.debug("Processing operation by processor {}.", processor.getClass().getName());
				processor.process(operation);
			});
			for (Handler handler : handlers) {
				log.debug("Handling operation by handler {}.", handler.getClass().getName());
				handler.handle(operation);
			}
			log.debug("Successfully finished operation chain.");
		} catch (RuntimeException e) {
			log.error("Exception in operation chain.", e);
		}
	}
}
