package de.ihrigb.fwla.fwlacenter.services.api;

public interface Weather {
	Coordinate getCoordinate();
	Wind getWind();
	float getTemperature();
	String getIconCode();
	String getDescription();
}
