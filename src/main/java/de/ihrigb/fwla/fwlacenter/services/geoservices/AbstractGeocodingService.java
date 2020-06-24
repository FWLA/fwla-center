package de.ihrigb.fwla.fwlacenter.services.geoservices;

import java.util.Optional;

import de.ihrigb.commons.Assert;
import de.ihrigb.fwla.fwlacenter.api.Address;
import de.ihrigb.fwla.fwlacenter.api.Coordinate;
import de.ihrigb.fwla.fwlacenter.api.Location;
import de.ihrigb.fwla.fwlacenter.services.api.GeocodingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
abstract class AbstractGeocodingService implements GeocodingService {

	private final GeoServiceProperties properties;

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

	private String stringify(Location location) {
		Assert.notNull(location, "Location must not be null.");
		return stringify(location.getAddress());
	}

	private String stringify(Address address) {
		Assert.notNull(address, "Address must not be null.");
		return String.format("%s %s %s", address.getStreet(), address.getTown(), properties.getCountry());
	}
}
