package de.ihrigb.fwla.fwlacenter.services.display;

import java.util.Optional;

import org.springframework.stereotype.Component;

import de.ihrigb.fwla.fwlacenter.services.api.DisplayService;
import de.ihrigb.fwla.fwlacenter.services.api.DisplayState;
import de.ihrigb.fwla.fwlacenter.services.api.Operation;
import de.ihrigb.fwla.fwlacenter.services.api.OperationService;
import de.ihrigb.fwla.fwlacenter.services.api.WeatherService;
import de.ihrigb.fwla.fwlacenter.services.api.DisplayState.State;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DisplayServiceImpl implements DisplayService {

	private final OperationService operationService;
	private final Optional<WeatherService> weatherService;

	@Override
	public DisplayState getDisplayState() {
		BaseDisplayState.BaseDisplayStateBuilder builder = BaseDisplayState.builder();

		if (operationService.hasActiveOperation()) {
			builder.state(State.OPERATION);
			Optional<Operation> activeOperation = operationService.getActiveOperation();
			builder.operation(activeOperation);
			activeOperation.ifPresent(o -> {
				weatherService.ifPresent(ws -> {
					builder.weather(Optional.ofNullable(ws.getWeather(o.getLocation().getCoordinate())));
				});
			});
		} else {
			builder.state(State.IDLE);
		}

		return builder.build();
	}
}
