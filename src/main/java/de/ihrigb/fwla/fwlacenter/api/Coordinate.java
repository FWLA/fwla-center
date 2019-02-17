package de.ihrigb.fwla.fwlacenter.api;

import java.util.Locale;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.geojson.Point;
import org.springframework.util.Assert;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public final class Coordinate implements Locatable {

	public static Coordinate of(Point point) {
		Assert.notNull(point, "Point must not be null.");
		double lat = point.getCoordinates().getLatitude();
		double lng = point.getCoordinates().getLongitude();
		return new Coordinate(lat, lng);
	}

	@Column(name = "latitude")
	private double latitude;
	@Column(name = "longitude")
	private double longitude;

	@Override
	public Optional<Coordinate> locate() {
		return Optional.of(this);
	}

	@Override
	public String toString() {
		return String.format(Locale.US, "[%f, %f]", this.latitude, this.longitude);
	}
}
