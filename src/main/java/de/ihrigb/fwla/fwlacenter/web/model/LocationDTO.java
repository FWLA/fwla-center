package de.ihrigb.fwla.fwlacenter.web.model;

import org.springframework.util.Assert;

import de.ihrigb.fwla.fwlacenter.services.api.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LocationDTO {

	private String street;
	private String town;
	private String district;
	private CoordinateDTO coordinate;

	public LocationDTO(Location location) {
		Assert.notNull(location, "Location must not be null");

		this.street = location.getStreet();
		this.town = location.getTown();
		this.district = location.getDistrict();
		if (location.getCoordinate() != null) {
			this.coordinate = new CoordinateDTO(location.getCoordinate());
		}
	}

	public Location getApiModel() {
		Location location = new Location();
		location.setStreet(street);
		location.setTown(town);
		location.setDistrict(district);
		if (coordinate != null) {
			location.setCoordinate(coordinate.getApiModel());
		}
		return location;
	}
}
