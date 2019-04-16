package de.ihrigb.fwla.fwlacenter.services.display;

import java.util.Optional;

import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

import de.ihrigb.fwla.fwlacenter.configuration.HomeProvider;
import de.ihrigb.fwla.fwlacenter.persistence.model.DisplayEvent;
import de.ihrigb.fwla.fwlacenter.persistence.model.Operation;
import de.ihrigb.fwla.fwlacenter.persistence.repository.DisplayEventRepository;
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

	private final Optional<BuildProperties> buildProperties;
	private final OperationService operationService;
	private final Optional<WeatherService> weatherService;
	private final Optional<HomeProvider> homeProvider;
	private final DisplayEventRepository displayEventRepository;

	@Override
	public DisplayState getDisplayState() {
		log.trace("getDisplayState()");

		DisplayState.DisplayStateBuilder builder = DisplayState.builder();

		buildProperties.ifPresent(p -> {
			builder.serverVersion(p.getVersion());
		});

		Optional<DisplayEvent> displayEventOpt = displayEventRepository.getActive();
		Optional<Operation> operationOpt = operationService.getActiveOperation();

		if (operationOpt.isPresent()) {
			Operation operation = operationOpt.get();
			if (!displayEventOpt.isPresent() || displayEventOpt.get().isShowOperation()) {
				log.debug("Display is in operation state.");
				builder.state(State.OPERATION);
				builder.operation(operationOpt);
				weatherService.ifPresent(ws -> {
					log.debug("Adding weather information to state.");
					builder.weather(Optional.ofNullable(ws.getWeather(operation.getLocation().getCoordinate())));
				});
			} else {
				DisplayEvent displayEvent = displayEventOpt.get();
				log.debug("Display is in text state.");
				builder.state(State.TEXT);
				builder.text(Optional.ofNullable(displayEvent.getText()));
			}
		} else if (displayEventOpt.isPresent()) {
			DisplayEvent displayEvent = displayEventOpt.get();
			log.debug("Display is in text state.");
			builder.state(State.TEXT);
			builder.text(Optional.ofNullable(displayEvent.getText()));
		} else {
			log.debug("Display is in idle state.");
			builder.state(State.IDLE);
		}

		homeProvider.ifPresent(hp -> {
			builder.home(Optional.ofNullable(hp.getHome()));
		});

		DisplayState displayState = builder.build();
		if (!displayState.getWeather().isPresent()) {
			homeProvider.ifPresent(hp -> {
				weatherService.ifPresent(ws -> {
					displayState.setWeather(Optional.ofNullable(ws.getWeather(hp.getHome())));
				});
			});
		}

		return displayState;
	}
}
