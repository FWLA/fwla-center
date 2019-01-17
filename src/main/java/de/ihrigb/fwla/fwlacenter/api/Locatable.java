package de.ihrigb.fwla.fwlacenter.api;

import java.util.Optional;

public interface Locatable {

	Optional<Coordinate> locate();
}
