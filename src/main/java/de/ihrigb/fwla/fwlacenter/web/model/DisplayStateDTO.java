package de.ihrigb.fwla.fwlacenter.web.model;

import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import de.ihrigb.fwla.fwlacenter.services.api.DisplayState;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class DisplayStateDTO {

	private String state;
	private String serverVersion;
	private OperationDTO operation;
	private WeatherDTO weather;
	private CoordinateDTO home;
	private String text;

	public DisplayStateDTO(DisplayState displayState) {
		Assert.notNull(displayState, "DisplayState must not be null.");
		this.state = displayState.getState().name();
		this.serverVersion = displayState.getServerVersion();
		this.operation = displayState.getOperation().map(o -> new OperationDTO(o)).orElse(null);
		this.weather = displayState.getWeather().map(w -> new WeatherDTO(w)).orElse(null);
		this.home = displayState.getHome().map(c -> new CoordinateDTO(c)).orElse(null);
		this.text = displayState.getText().orElse(null);
	}
}