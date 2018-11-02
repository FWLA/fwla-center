package de.ihrigb.fwla.fwlacenter.services.weather;

import java.net.URI;
import java.util.List;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.ihrigb.fwla.fwlacenter.services.api.Coordinate;
import de.ihrigb.fwla.fwlacenter.services.api.Weather;
import de.ihrigb.fwla.fwlacenter.services.api.WeatherService;
import de.ihrigb.fwla.fwlacenter.services.api.Wind;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpenWeatherMapWeatherService implements WeatherService {

	static final String BASE_URI = "https://api.openweathermap.org/data/2.5";

	private final RestTemplate restTemplate;
	private final OpenWeatherMapProperties properties;

	public OpenWeatherMapWeatherService(RestTemplateBuilder restTemplateBuilder, OpenWeatherMapProperties properties) {
		this.restTemplate = restTemplateBuilder.setConnectTimeout((int) properties.getConnectTimeout().toMillis())
				.setReadTimeout((int) properties.getReadTimeout().toMillis()).build();
		this.properties = properties;
	}

	@Override
	public Weather getWeather(Coordinate coordinate) {
		URI uri = UriComponentsBuilder.fromUriString(BASE_URI).path("/weather")
				.queryParam("lat", coordinate.getLatitude()).queryParam("lon", coordinate.getLongitude())
				.queryParam("units", "metric").queryParam("lang", "de").queryParam("APPID", properties.getApiKey())
				.build().toUri();

		try {
			return doExecute(uri);
		} catch (RestClientException e) {
			log.error("Exception while getting weather from OWM.", e);
			return null;
		}
	}

	@Retryable(include = ResourceAccessException.class, maxAttempts = 2, backoff = @Backoff(2000), label = "openweathermaps-retry")
	Weather doExecute(URI uri) {
		OWMWeather owmWeather = restTemplate.getForObject(uri, OWMWeather.class);

		return new Weather() {
			@Getter
			private final Wind wind = new Wind() {

				@Getter
				private final float speed = owmWeather.getWind().getSpeed();
				@Getter
				private final int degrees = owmWeather.getWind().getDeg();
			};

			@Getter
			private final Coordinate coordinate = new Coordinate(owmWeather.getCoord().getLat(),
					owmWeather.getCoord().getLon());

			@Getter
			private final float temperature = owmWeather.getMain().getTemp();
			@Getter
			private final String iconCode = owmWeather.getWeather().get(0).getIcon();
			@Getter
			private final String description = owmWeather.getWeather().get(0).getDescription();
		};
	}

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	static class OWMWeather {
		private OWMCoord coord;
		private OWMMain main;
		private OWMWind wind;
		private List<OWMWeatherMeta> weather;
	}

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	static class OWMCoord {
		private double lat;
		private double lon;
	}

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	static class OWMMain {
		private float temp;
	}

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	static class OWMWind {
		private float speed;
		private int deg;
	}

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	static class OWMWeatherMeta {
		private String icon;
		private String description;
	}
}
