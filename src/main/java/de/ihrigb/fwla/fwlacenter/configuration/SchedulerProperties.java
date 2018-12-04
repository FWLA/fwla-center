package de.ihrigb.fwla.fwlacenter.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.scheduler")
public class SchedulerProperties {
	private int poolSize = 10;
}
