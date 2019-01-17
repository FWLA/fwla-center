package de.ihrigb.fwla.fwlacenter.services.api;

import java.util.Optional;

import org.geojson.FeatureCollection;

import de.ihrigb.fwla.fwlacenter.api.Coordinate;

public interface DirectionsService {

	Optional<FeatureCollection> getDirections(Coordinate from, Coordinate to);
}
