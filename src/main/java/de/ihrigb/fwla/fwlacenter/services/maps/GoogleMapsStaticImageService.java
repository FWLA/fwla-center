package de.ihrigb.fwla.fwlacenter.services.maps;

import java.net.URI;
import java.util.Locale;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import de.ihrigb.fwla.fwlacenter.services.api.Coordinate;
import de.ihrigb.fwla.fwlacenter.services.api.Dimensions;
import de.ihrigb.fwla.fwlacenter.services.api.MapsImageService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GoogleMapsStaticImageService implements MapsImageService {

	static final String BASE_URI = "https://maps.googleapis.com/maps/api/staticmap";

	static final int DEFAULT_ZOOM = 16;
	static final Dimensions DEFAULT_DIMENSIONS = new DimensionsImpl(640, 640);
	static final int DEFAULT_SCALE = 1;

	private static String stringify(Coordinate coordinate) {
		return String.format(Locale.US, "%f,%f", coordinate.getLatitude(), coordinate.getLongitude());
	}

	private static String stringify(Dimensions dimensions) {
		return String.format(Locale.US, "%dx%d", dimensions.getX(), dimensions.getY());
	}

	private final RestTemplate restTemplate;
	private final GoogleMapsProperties properties;

	public GoogleMapsStaticImageService(RestTemplateBuilder restTemplateBuilder, GoogleMapsProperties properties) {
		this.restTemplate = restTemplateBuilder.setConnectTimeout((int) properties.getConnectTimeout().toMillis())
				.setReadTimeout((int) properties.getReadTimeout().toMillis()).build();
		this.properties = properties;
	}

	@Override
	public byte[] getImage(Coordinate coordinate) {
		return getImage(coordinate, DEFAULT_ZOOM);
	}

	@Override
	public byte[] getImage(Coordinate coordinate, int zoom) {
		return getImage(coordinate, zoom, DEFAULT_DIMENSIONS);
	}

	@Override
	public byte[] getImage(Coordinate coordinate, int zoom, Dimensions size) {
		return getImage(coordinate, zoom, size, DEFAULT_SCALE);
	}

	@Override
	public byte[] getImage(Coordinate coordinate, int zoom, Dimensions size, int scale) {

		String centerValue = stringify(coordinate);
		String sizeValue = stringify(scaleDimensions(size));
		String markersValue = new Marker(coordinate).toString();

		URI uri = UriComponentsBuilder.fromHttpUrl(BASE_URI).queryParam("center", centerValue).queryParam("zoom", zoom)
				.queryParam("size", sizeValue).queryParam("scale", scale).queryParam("markers", markersValue)
				.queryParam("key", properties.getApiKey()).build().toUri();

		try {
			return doExecute(uri);
		} catch (RestClientException e) {
			log.error("Getting google static maps failed with exception.", e);
			return null;
		}
	}

	@Retryable(include = ResourceAccessException.class, maxAttempts = 2, backoff = @Backoff(2000), label = "google-static-maps-retry")
	byte[] doExecute(URI uri) {
		return restTemplate.getForObject(uri, byte[].class);
	}

	private Dimensions scaleDimensions(Dimensions dimensions) {
		if (dimensions.getX() <= 640 && dimensions.getY() <= 640) {
			return dimensions;
		}
		float factor;
		if (dimensions.getX() > 640) {
			factor = getFactor(dimensions.getX());
		} else {
			factor = getFactor(dimensions.getY());
		}
		int x = Math.round(factor * dimensions.getX());
		int y = Math.round(factor * dimensions.getY());

		return new DimensionsImpl(x, y);
	}

	private float getFactor(int scale) {
		return 640f / scale;
	}

	@Getter
	@Setter
	@RequiredArgsConstructor
	static class DimensionsImpl implements Dimensions {
		private final int x;
		private final int y;
	}

	@RequiredArgsConstructor
	static class Marker {
		private final Coordinate coordinate;

		@Override
		public String toString() {
			return String.format("color:red|%s", GoogleMapsStaticImageService.stringify(coordinate));
		}
	}
}
