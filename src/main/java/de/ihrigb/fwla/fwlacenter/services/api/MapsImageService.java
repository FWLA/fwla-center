package de.ihrigb.fwla.fwlacenter.services.api;

public interface MapsImageService {
	byte[] getImage(Coordinate coordinate);

	byte[] getImage(Coordinate coordinate, int zoom);

	byte[] getImage(Coordinate coordinate, int zoom, Dimensions size);

	byte[] getImage(Coordinate coordinate, int zoom, Dimensions size, int scale);
}
