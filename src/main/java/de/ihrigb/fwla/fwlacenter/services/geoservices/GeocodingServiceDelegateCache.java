package de.ihrigb.fwla.fwlacenter.services.geoservices;

import java.util.Optional;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import de.ihrigb.fwla.fwlacenter.api.Coordinate;
import de.ihrigb.fwla.fwlacenter.services.api.GeocodingService;

public class GeocodingServiceDelegateCache extends AbstractGeocodingService implements GeocodingService {

	private final GeocodingService delegate;
	private final Cache<String, Optional<Coordinate>> cache;

	public GeocodingServiceDelegateCache(GeocodingService delegate, GeoServiceProperties properties) {
		super(properties);
		this.delegate = delegate;
		this.cache = Caffeine.newBuilder().expireAfterWrite(properties.getGeocodingCache().getExpireAfterWrite())
				.build();
	}

	@Override
	public Optional<Coordinate> geocode(String value) {
		return this.cache.get(value, key -> {
			return delegate.geocode(key);
		});
	}
}
