package de.ihrigb.fwla.fwlacenter.services.operation;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.operation")
public class OperationProperties {
	private Duration timeout = Duration.ofMinutes(15);
}
