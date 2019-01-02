package de.ihrigb.fwla.fwlacenter.services.geoservices;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.geo.ors")
public class OpenRouteServiceProperties {

	private String apiKey;
	private String country = "Deutschland";
	private Duration readTimeout = Duration.ofSeconds(5);
	private Duration connectTimeout = Duration.ofSeconds(10);
}
