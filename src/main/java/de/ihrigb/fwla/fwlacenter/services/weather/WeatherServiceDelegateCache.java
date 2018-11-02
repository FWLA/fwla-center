package de.ihrigb.fwla.fwlacenter.services.weather;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import de.ihrigb.fwla.fwlacenter.services.api.Coordinate;
import de.ihrigb.fwla.fwlacenter.services.api.Weather;
import de.ihrigb.fwla.fwlacenter.services.api.WeatherService;

public class WeatherServiceDelegateCache implements WeatherService {

	private final WeatherService delegate;
	private final Cache<Coordinate, Weather> cache;

	public WeatherServiceDelegateCache(WeatherService delegate, WeatherServiceProperties properties) {
		this.delegate = delegate;
		this.cache = Caffeine.newBuilder().expireAfterWrite(properties.getCache().getExpireAfterWrite()).build();
	}

	@Override
	public Weather getWeather(Coordinate coordinate) {
		return cache.get(coordinate, c -> {
			return delegate.getWeather(c);
		});
	}
}
