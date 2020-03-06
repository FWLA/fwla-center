package de.ihrigb.fwla.fwlacenter.services.geoservices;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import de.ihrigb.fwla.fwlacenter.api.Coordinate;
import de.ihrigb.fwla.fwlacenter.services.geoservices.GeoServiceProperties.OpenRouteServiceProperties;
import de.ihrigb.fwla.fwlacenter.services.geoservices.visitors.PointVisitorPredicate;
import de.ihrigb.fwla.fwlacenter.services.geoservices.visitors.ToCoordinateFunction;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpenRouteServiceGeocodingService extends AbstractGeocodingService {

	private final static String baseUri = "https://api.openrouteservice.org/geocode/search";

	private final OpenRouteServiceProperties properties;
	private final RestTemplate restTemplate;

	public OpenRouteServiceGeocodingService(GeoServiceProperties properties, RestTemplateBuilder restTemplateBuilder) {
		super(properties);
		this.properties = properties.getOrs();
		this.restTemplate = restTemplateBuilder.setConnectTimeout(properties.getOrs().getConnectTimeout())
				.setReadTimeout(properties.getOrs().getReadTimeout()).build();
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

	private String encode(String query) {
		return query.trim().replace(" ", "+");
	}
}
