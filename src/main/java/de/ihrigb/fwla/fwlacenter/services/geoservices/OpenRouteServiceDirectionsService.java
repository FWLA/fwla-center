package de.ihrigb.fwla.fwlacenter.services.geoservices;

import java.net.URI;
import java.util.Locale;
import java.util.Optional;

import org.geojson.FeatureCollection;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import de.ihrigb.fwla.fwlacenter.api.Coordinate;
import de.ihrigb.fwla.fwlacenter.services.api.DirectionsService;
import de.ihrigb.fwla.fwlacenter.services.geoservices.GeoServiceProperties.OpenRouteServiceProperties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpenRouteServiceDirectionsService implements DirectionsService {

	private final static String baseUri = "https://api.openrouteservice.org/directions";

	private final RestTemplate restTemplate;
	private final OpenRouteServiceProperties properties;

	public OpenRouteServiceDirectionsService(RestTemplateBuilder restTemplateBuilder,
			OpenRouteServiceProperties properties) {
		this.restTemplate = restTemplateBuilder.setConnectTimeout(properties.getConnectTimeout())
				.setReadTimeout(properties.getReadTimeout()).build();
		this.properties = properties;
	}

	@Override
	public Optional<FeatureCollection> getDirections(Coordinate from, Coordinate to) {
		URI uri = UriComponentsBuilder.fromUriString(baseUri).queryParam("api_key", properties.getApiKey())
				.queryParam("coordinates", stringifyCoordinates(from, to)).queryParam("profile", "driving-car")
				.queryParam("format", "geojson").queryParam("geometry_format", "geojson")
				.queryParam("instructions", false).build().toUri();

		log.debug("Directions URI: {}", uri);

		try {
			return Optional.of(restTemplate.getForObject(uri, FeatureCollection.class));
		} catch (RestClientException e) {
			log.warn("Exception while calling directions service.", e);
			return Optional.empty();
		}
	}

	private String stringifyCoordinates(Coordinate from, Coordinate to) {
		return String.format(Locale.US, "%f,%f|%f,%f", from.getLongitude(), from.getLatitude(), to.getLongitude(),
				to.getLatitude());
	}
}
