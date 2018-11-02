package de.ihrigb.fwla.fwlacenter.services.maps;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.maps")
public class MapsImageServiceProperties {
	private MapsCache cache = new MapsCache();

	@Getter
	@Setter
	public static class MapsCache {
		private Duration expireAfterWrite = Duration.ofHours(8);
	}
}
