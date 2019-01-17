package de.ihrigb.fwla.fwlacenter.services.api;

import de.ihrigb.fwla.fwlacenter.api.Coordinate;

public interface WeatherService {
	Weather getWeather(Coordinate coordinate);
}
