package de.ihrigb.fwla.fwlacenter.services.maps;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import de.ihrigb.fwla.fwlacenter.services.api.Coordinate;
import de.ihrigb.fwla.fwlacenter.services.api.Dimensions;
import de.ihrigb.fwla.fwlacenter.services.api.MapsImageService;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class MapsImageServiceCacheDelegate implements MapsImageService {

	private final MapsImageService delegate;
	private final Cache<Key, byte[]> cache;

	public MapsImageServiceCacheDelegate(MapsImageService delegate, MapsImageServiceProperties properties) {
		this.delegate = delegate;
		this.cache = Caffeine.newBuilder().expireAfterWrite(properties.getCache().getExpireAfterWrite()).build();
	}

	@Override
	public byte[] getImage(Coordinate coordinate) {
		return cache.get(new Key(coordinate, null, null, null), key -> {
			return delegate.getImage(key.getCoordinate());
		});
	}

	@Override
	public byte[] getImage(Coordinate coordinate, int zoom) {
		return cache.get(new Key(coordinate, zoom, null, null), key -> {
			return delegate.getImage(key.getCoordinate(), key.getZoom());
		});
	}

	@Override
	public byte[] getImage(Coordinate coordinate, int zoom, Dimensions size) {
		return cache.get(new Key(coordinate, zoom, size, null), key -> {
			return delegate.getImage(key.getCoordinate(), key.getZoom(), key.getSize());
		});
	}

	@Override
	public byte[] getImage(Coordinate coordinate, int zoom, Dimensions size, int scale) {
		return cache.get(new Key(coordinate, zoom, size, scale), key -> {
			return delegate.getImage(key.getCoordinate(), key.getZoom(), key.getSize(), key.getScale());
		});
	}

	@Getter
	@EqualsAndHashCode
	@RequiredArgsConstructor
	static class Key {
		private final Coordinate coordinate;
		private final Integer zoom;
		private final Dimensions size;
		private final Integer scale;
	}
}
