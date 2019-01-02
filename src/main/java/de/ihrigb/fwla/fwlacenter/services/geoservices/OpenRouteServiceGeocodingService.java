package de.ihrigb.fwla.fwlacenter.services.geoservices;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import de.ihrigb.fwla.fwlacenter.persistence.model.Address;
import de.ihrigb.fwla.fwlacenter.services.api.Coordinate;
import de.ihrigb.fwla.fwlacenter.services.api.GeocodingService;
import de.ihrigb.fwla.fwlacenter.services.api.Location;
import de.ihrigb.fwla.fwlacenter.services.geoservices.visitors.PointVisitorPredicate;
import de.ihrigb.fwla.fwlacenter.services.geoservices.visitors.ToCoordinateFunction;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "app.geo.ors", name = "apiKey", matchIfMissing = false)
@EnableConfigurationProperties(OpenRouteServiceProperties.class)
public class OpenRouteServiceGeocodingService implements GeocodingService {

	private final static String baseUri = "https://api.openrouteservice.org/geocode/search";

	private final OpenRouteServiceProperties properties;
	private final RestTemplate restTemplate;

	public OpenRouteServiceGeocodingService(OpenRouteServiceProperties properties,
			RestTemplateBuilder restTemplateBuilder) {
		this.properties = properties;
		this.restTemplate = restTemplateBuilder.setConnectTimeout(properties.getConnectTimeout())
				.setReadTimeout(properties.getReadTimeout()).build();
	}

	@Override
	public Optional<Coordinate> geocode(Address address) {
		return geocode(stringify(address));
	}

	@Override
	public void geocode(Location location) {
		geocode(stringify(location)).ifPresent(coordinate -> {
			location.setCoordinate(coordinate);
		});
	}

	@Override
	public Optional<Coordinate> geocode(String query) {
		URI uri = UriComponentsBuilder.fromUriString(baseUri).queryParam("api_key", properties.getApiKey())
				.queryParam("text", encode(query)).build().toUri();

		log.info("Geocoding URI: {}", uri);

		try {
			FeatureCollection featureCollection = restTemplate.getForObject(uri, FeatureCollection.class);
			List<Feature> features = featureCollection.getFeatures();
			if (features == null || features.isEmpty()) {
				return Optional.empty();
			}
			return features.stream().filter(Objects::nonNull).filter(new PointVisitorPredicate()).findFirst()
					.map(new ToCoordinateFunction());
		} catch (RestClientException e) {
			log.warn("Exception while calling directions service.", e);
			return Optional.empty();
		}
	}

	private String stringify(Location location) {
		return String.format("%s %s %s", location.getStreet(), location.getTown(), properties.getCountry());
	}

	private String stringify(Address address) {
		return String.format("%s %s %s", address.getStreet(), address.getTown(), properties.getCountry());
	}

	private String encode(String query) {
		return query.trim().replace(" ", "+");
	}
}
