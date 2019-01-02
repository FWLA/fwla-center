package de.ihrigb.fwla.fwlacenter.services.api;

import java.util.Optional;

/**
 * Central entrypoint for geo services.
 */
public interface GeoServices {

	/**
	 * Get the directions service, if present.
	 *
	 * @return optional of directions service
	 */
	Optional<DirectionsService> directions();

	/**
	 * Get the geocoding service, if present.
	 *
	 * @return optional of geocoding service
	 */
	Optional<GeocodingService> geocoding();
}
