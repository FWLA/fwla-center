package de.ihrigb.fwla.fwlacenter.services.geoservices;

import java.util.Optional;

import org.geojson.FeatureCollection;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import de.ihrigb.fwla.fwlacenter.api.Coordinate;
import de.ihrigb.fwla.fwlacenter.services.api.DirectionsService;
import de.ihrigb.fwla.fwlacenter.services.geoservices.GeoServiceProperties.CacheProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class DirectionsServiceDelegateCache implements DirectionsService {

	private final DirectionsService delegate;
	private final Cache<CacheKey, Optional<FeatureCollection>> cache;

	public DirectionsServiceDelegateCache(DirectionsService delegate, CacheProperties properties) {
		this.delegate = delegate;
		this.cache = Caffeine.newBuilder().expireAfterWrite(properties.getExpireAfterWrite())
				.build();
	}

	@Override
	public Optional<FeatureCollection> getDirections(Coordinate from, Coordinate to) {
		return cache.get(new CacheKey(from, to), cacheKey -> {
			return delegate.getDirections(cacheKey.getFrom(), cacheKey.getTo());
		});
	}

	@Getter
	@RequiredArgsConstructor
	@EqualsAndHashCode
	class CacheKey {
		private final Coordinate from;
		private final Coordinate to;
	}
}
