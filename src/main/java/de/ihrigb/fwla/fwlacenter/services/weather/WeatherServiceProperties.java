package de.ihrigb.fwla.fwlacenter.services.weather;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.weather")
public class WeatherServiceProperties {

	private WeatherCache cache = new WeatherCache();

	@Getter
	@Setter
	public static class WeatherCache {
		private Duration expireAfterWrite = Duration.ofHours(1);
	}
}
