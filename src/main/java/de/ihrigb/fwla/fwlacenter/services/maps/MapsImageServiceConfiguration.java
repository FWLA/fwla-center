package de.ihrigb.fwla.fwlacenter.services.maps;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.ihrigb.fwla.fwlacenter.services.api.MapsImageService;

@Configuration
@EnableConfigurationProperties({ MapsImageServiceProperties.class, GoogleMapsProperties.class })
@ConditionalOnProperty(prefix = "app.maps.google", name = "apiKey", matchIfMissing = false)
public class MapsImageServiceConfiguration {

	@Bean
	public MapsImageService googleStaticMapsImageService(RestTemplateBuilder restTemplateBuilder,
			GoogleMapsProperties googleProperties, MapsImageServiceProperties serviceProperties) {
		return new MapsImageServiceCacheDelegate(
				new GoogleMapsStaticImageService(restTemplateBuilder, googleProperties), serviceProperties);
	}
}
