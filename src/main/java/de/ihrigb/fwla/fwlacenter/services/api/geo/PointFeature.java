package de.ihrigb.fwla.fwlacenter.services.api.geo;

import de.ihrigb.fwla.fwlacenter.api.Address;
import de.ihrigb.fwla.fwlacenter.api.Coordinate;
import de.ihrigb.fwla.fwlacenter.api.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PointFeature extends Feature {
	private Coordinate coordinate;
	private Address address;
	private String color;

	public PointFeature(String name, String text, String color, Location location) {
		super("point", name, text, color);
		this.coordinate = location.getCoordinate();
		this.address = location.getAddress();
	}
}
