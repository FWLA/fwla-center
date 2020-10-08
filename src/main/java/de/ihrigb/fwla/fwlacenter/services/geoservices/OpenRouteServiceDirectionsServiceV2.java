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
public class OpenRouteServiceDirectionsServiceV2 implements DirectionsService {

	private final static String baseUri = "https://api.openrouteservice.org/v2/directions/driving-car";

	private final RestTemplate restTemplate;
	private final OpenRouteServiceProperties properties;

	public OpenRouteServiceDirectionsServiceV2(RestTemplateBuilder restTemplateBuilder,
			OpenRouteServiceProperties properties) {
		this.restTemplate = restTemplateBuilder.setConnectTimeout(properties.getConnectTimeout())
				.setReadTimeout(properties.getReadTimeout()).build();
		this.properties = properties;
	}

	@Override
	public Optional<FeatureCollection> getDirections(Coordinate from, Coordinate to) {
		URI uri = UriComponentsBuilder.fromUriString(baseUri).queryParam("api_key", properties.getApiKey())
				.queryParam("start", stringifyCoordinate(from)).queryParam("end", stringifyCoordinate(to)).build()
				.toUri();

		log.debug("Directions URI: {}", uri);

		try {
			return Optional.of(restTemplate.getForObject(uri, FeatureCollection.class));
		} catch (RestClientException e) {
			log.warn("Exception while calling directions service.", e);
			return Optional.empty();
		}
	}

	private String stringifyCoordinate(Coordinate c) {
		return String.format(Locale.US, "%f,%f", c.getLongitude(), c.getLatitude());
	}

	public static void main(String[] args) {
		OpenRouteServiceProperties properties = new OpenRouteServiceProperties();
		properties.setApiKey("5b3ce3597851110001cf62489442932f2c474fb4acd6ba26ce3e5fa0");

		RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();

		OpenRouteServiceDirectionsServiceV2 directionsService = new OpenRouteServiceDirectionsServiceV2(
				restTemplateBuilder, properties);

		Optional<FeatureCollection> geojson = directionsService.getDirections(new Coordinate(49.591684, 8.480834),
				new Coordinate(49.598678, 8.460858));

		System.out.println(geojson.map(FeatureCollection::toString).orElseThrow(RuntimeException::new));
	}
}
