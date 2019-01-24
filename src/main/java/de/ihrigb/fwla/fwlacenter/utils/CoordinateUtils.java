package de.ihrigb.fwla.fwlacenter.utils;

import org.springframework.util.Assert;

import de.ihrigb.fwla.fwlacenter.api.Coordinate;

public final class CoordinateUtils {

	public static final double R = 6372800; // in meters

	public static double haversine(Coordinate c1, Coordinate c2) {
		Assert.notNull(c1, "Coordinate must not be null.");
		Assert.notNull(c2, "Coordinate must not be null.");

		double lat1 = c1.getLatitude();
		double lon1 = c1.getLongitude();
		double lat2 = c2.getLatitude();
		double lon2 = c2.getLongitude();

		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);

		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);

		double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
		double c = 2 * Math.asin(Math.sqrt(a));

		return R * c;
	}

	private CoordinateUtils() {}
}
