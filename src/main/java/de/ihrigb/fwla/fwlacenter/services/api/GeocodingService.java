package de.ihrigb.fwla.fwlacenter.services.api;

import java.util.Optional;

import de.ihrigb.fwla.fwlacenter.persistence.model.Address;

/**
 * Service to geocode locations.
 */
public interface GeocodingService {

	/**
	 * Geocode a string value.
	 *
	 * @param value the value to be geocoded.
	 * @return optional of coordinate
	 */
	Optional<Coordinate> geocode(String value);

	/**
	 * Geocode an address.
	 *
	 * @param address the address to be geocoded.
	 * @return optional of coordinate
	 */
	Optional<Coordinate> geocode(Address address);

	/**
	 * Geocode and set coordinate to location, if found.
	 *
	 * @param location the location to be geocoded and coordinate to be set
	 */
	void geocode(Location location);
}
