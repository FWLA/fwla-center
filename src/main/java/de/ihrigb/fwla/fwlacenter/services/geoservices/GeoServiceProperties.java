package de.ihrigb.fwla.fwlacenter.services.geoservices;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.geo")
public class GeoServiceProperties {

	private OpenRouteServiceProperties ors;
	private CacheProperties directionsCache = new CacheProperties();
	private CacheProperties geocodingCache = new CacheProperties();
	private String country = "Deutschland";

	@Getter
	@Setter
	public static class CacheProperties {
		private Duration expireAfterWrite = Duration.ofHours(1);
	}

	@Getter
	@Setter
	public static class OpenRouteServiceProperties {

		private String apiKey;
		private Duration readTimeout = Duration.ofSeconds(5);
		private Duration connectTimeout = Duration.ofSeconds(10);
	}
}
