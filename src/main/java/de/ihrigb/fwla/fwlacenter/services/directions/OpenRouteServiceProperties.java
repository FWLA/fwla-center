package de.ihrigb.fwla.fwlacenter.services.directions;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.directions.ors")
public class OpenRouteServiceProperties {

	private String apiKey;
	private Duration readTimeout = Duration.ofSeconds(5);
	private Duration connectTimeout = Duration.ofSeconds(10);
}
