package de.ihrigb.fwla.fwlacenter.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import de.ihrigb.fwla.fwlacenter.services.api.Coordinate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.home")
public class HomeProperties {
	private Coordinate coordinate;
}
