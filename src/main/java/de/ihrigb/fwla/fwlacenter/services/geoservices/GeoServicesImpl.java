package de.ihrigb.fwla.fwlacenter.services.geoservices;

import java.util.Optional;

import de.ihrigb.fwla.fwlacenter.services.api.DirectionsService;
import de.ihrigb.fwla.fwlacenter.services.api.GeoServices;
import de.ihrigb.fwla.fwlacenter.services.api.GeocodingService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GeoServicesImpl implements GeoServices {

	private final Optional<DirectionsService> directions;
	private final Optional<GeocodingService> geocoding;

	@Override
	public Optional<DirectionsService> directions() {
		return directions;
	}

	@Override
	public Optional<GeocodingService> geocoding() {
		return geocoding;
	}
}
