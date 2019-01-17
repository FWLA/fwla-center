package de.ihrigb.fwla.fwlacenter.services.api.geo;

import de.ihrigb.fwla.fwlacenter.api.Address;
import de.ihrigb.fwla.fwlacenter.api.Coordinate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PointFeature extends Feature {
	private Coordinate coordinate;
	private Address address;

	public PointFeature(String name, String text, Coordinate coordinate, Address address) {
		super(name, text);
		this.coordinate = coordinate;
		this.address = address;
	}
}
