package de.ihrigb.fwla.fwlacenter.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import de.ihrigb.fwla.fwlacenter.api.Coordinate;
import lombok.RequiredArgsConstructor;

@Component
@EnableConfigurationProperties(HomeProperties.class)
@RequiredArgsConstructor
public final class HomeProvider {

	private final HomeProperties properties;

	public Coordinate getHome() {
		return properties.getCoordinate();
	}
}
