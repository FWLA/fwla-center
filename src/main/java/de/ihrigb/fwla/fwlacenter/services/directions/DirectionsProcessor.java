package de.ihrigb.fwla.fwlacenter.services.directions;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import de.ihrigb.fwla.fwlacenter.configuration.HomeProvider;
import de.ihrigb.fwla.fwlacenter.handling.api.Processor;
import de.ihrigb.fwla.fwlacenter.services.api.DirectionsService;
import de.ihrigb.fwla.fwlacenter.services.api.Operation;
import lombok.RequiredArgsConstructor;

@Component
@ConditionalOnBean(DirectionsService.class)
@RequiredArgsConstructor
public class DirectionsProcessor implements Processor {

	private final DirectionsService directionsService;
	private final HomeProvider homeProvider;

	@Override
	public void process(Operation operation) {
		directionsService.getDirections(homeProvider.getHome(), operation.getLocation().getCoordinate())
				.ifPresent(directions -> {
					operation.setDirections(directions);
				});
	}
}
