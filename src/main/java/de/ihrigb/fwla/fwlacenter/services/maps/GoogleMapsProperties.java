package de.ihrigb.fwla.fwlacenter.services.maps;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.maps.google")
public class GoogleMapsProperties {

	private String apiKey;
	private Duration connectTimeout = Duration.ofSeconds(5);
	private Duration readTimeout = Duration.ofSeconds(10);
}
