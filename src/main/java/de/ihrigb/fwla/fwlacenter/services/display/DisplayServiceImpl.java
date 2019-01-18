package de.ihrigb.fwla.fwlacenter.services.display;

import java.util.Optional;

import org.springframework.stereotype.Component;

import de.ihrigb.fwla.fwlacenter.configuration.HomeProvider;
import de.ihrigb.fwla.fwlacenter.persistence.model.Operation;
import de.ihrigb.fwla.fwlacenter.services.api.DisplayService;
import de.ihrigb.fwla.fwlacenter.services.api.DisplayState;
import de.ihrigb.fwla.fwlacenter.services.api.DisplayState.State;
import de.ihrigb.fwla.fwlacenter.services.api.OperationService;
import de.ihrigb.fwla.fwlacenter.services.api.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DisplayServiceImpl implements DisplayService {

	private final OperationService operationService;
	private final Optional<WeatherService> weatherService;
	private final Optional<HomeProvider> homeProvider;

	@Override
	public DisplayState getDisplayState() {
		log.trace("getDisplayState()");

		DisplayState.DisplayStateBuilder builder = DisplayState.builder();

		if (operationService.hasActiveOperation()) {
			log.debug("Display is in operation state.");
			builder.state(State.OPERATION);
			Optional<Operation> activeOperation = operationService.getActiveOperation();
			builder.operation(activeOperation);
			activeOperation.ifPresent(o -> {
				weatherService.ifPresent(ws -> {
					log.debug("Adding weather information to state.");
					builder.weather(Optional.ofNullable(ws.getWeather(o.getLocation().getCoordinate())));
				});
			});
		} else {
			log.debug("Display is in idle state.");
			builder.state(State.IDLE);
		}

		homeProvider.ifPresent(hp -> {
			builder.home(Optional.ofNullable(hp.getHome()));
		});

		return builder.build();
	}
}
