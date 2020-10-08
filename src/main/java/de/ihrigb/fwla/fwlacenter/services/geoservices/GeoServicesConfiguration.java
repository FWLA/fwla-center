package de.ihrigb.fwla.fwlacenter.services.geoservices;

import java.util.Optional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.ihrigb.fwla.fwlacenter.services.api.DirectionsService;
import de.ihrigb.fwla.fwlacenter.services.api.GeoServices;
import de.ihrigb.fwla.fwlacenter.services.api.GeocodingService;

@Configuration
@EnableConfigurationProperties(GeoServiceProperties.class)
public class GeoServicesConfiguration {

	@Bean
	@ConditionalOnProperty(prefix = "app.geo.ors", name = "api-key", matchIfMissing = false)
	public DirectionsService directionsService(GeoServiceProperties properties,
			RestTemplateBuilder restTemplateBuilder) {
		return new DirectionsServiceDelegateCache(
				new OpenRouteServiceDirectionsServiceV2(restTemplateBuilder, properties.getOrs()),
				properties.getDirectionsCache());
	}

	@Bean
	@ConditionalOnProperty(prefix = "app.geo.ors", name = "api-key", matchIfMissing = false)
	public GeocodingService geocodingService(GeoServiceProperties properties, RestTemplateBuilder restTemplateBuilder) {
		return new GeocodingServiceDelegateCache(new OpenRouteServiceGeocodingService(properties, restTemplateBuilder),
				properties);
	}

	@Bean
	public GeoServices geoServices(Optional<DirectionsService> directionsService,
			Optional<GeocodingService> geocodingService) {
		return new GeoServicesImpl(directionsService, geocodingService);
	}
}
