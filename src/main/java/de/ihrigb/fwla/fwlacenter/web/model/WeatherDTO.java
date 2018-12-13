package de.ihrigb.fwla.fwlacenter.web.model;

import org.springframework.util.Assert;

import de.ihrigb.fwla.fwlacenter.services.api.Weather;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherDTO {

	private CoordinateDTO coordinate;
	private WindDTO wind;
	private float temperature;
	private String iconCode;
	private String description;

	public WeatherDTO(Weather weather) {
		Assert.notNull(weather, "Weather must not be null.");
		if (weather.getCoordinate() != null) {
			this.coordinate = new CoordinateDTO(weather.getCoordinate());
		}
		if (weather.getWind() != null) {
			this.wind = new WindDTO(weather.getWind());
		}
		this.temperature = weather.getTemperature();
		this.iconCode = weather.getIconCode();
		this.description = weather.getDescription();
	}
}
