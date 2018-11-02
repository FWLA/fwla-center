package de.ihrigb.fwla.fwlacenter.services.weather;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

import de.ihrigb.fwla.fwlacenter.services.api.WeatherService;

@Configuration
@EnableConfigurationProperties({ WeatherServiceProperties.class, OpenWeatherMapProperties.class })
@EnableRetry
@ConditionalOnProperty(prefix = "app.weather.openweathermap", name = "apiKey", matchIfMissing = false)
public class WeatherServiceConfiguration {

	@Bean
	public WeatherService owmWeatherService(RestTemplateBuilder restTemplateBuilder,
			WeatherServiceProperties serviceProperties, OpenWeatherMapProperties owmProperties) {
		return new WeatherServiceDelegateCache(new OpenWeatherMapWeatherService(restTemplateBuilder, owmProperties),
				serviceProperties);
	}
}
