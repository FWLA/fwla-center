package de.ihrigb.fwla.fwlacenter.services.api;

import de.ihrigb.fwla.fwlacenter.api.Coordinate;

public interface Weather {
	Coordinate getCoordinate();
	Wind getWind();
	float getTemperature();
	String getIconCode();
	String getDescription();
}
