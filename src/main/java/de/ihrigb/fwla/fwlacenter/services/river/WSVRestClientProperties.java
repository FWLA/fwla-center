package de.ihrigb.fwla.fwlacenter.services.river;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.river.wsv")
public class WSVRestClientProperties {

	private Duration connectTimeout = Duration.ofSeconds(2);

	private Duration readTimeout = Duration.ofSeconds(60);

	private Duration cacheTimeout = Duration.ofDays(365);
}
