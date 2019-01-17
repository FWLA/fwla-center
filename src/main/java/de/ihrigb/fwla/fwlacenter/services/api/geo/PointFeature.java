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

	public PointFeature(String name, String text, Location location, String color) {
		super(name, text);
		this.coordinate = location.getCoordinate();
		this.address = location.getAddress();
		this.color = color;
	}
}
