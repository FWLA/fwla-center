package de.ihrigb.fwla.fwlacenter.web.model;

import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.ihrigb.fwla.fwlacenter.api.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LocationDTO {

	private AddressDTO address;
	private CoordinateDTO coordinate;

	public LocationDTO(Location location) {
		Assert.notNull(location, "Location must not be null");

		if (location.getAddress() != null) {
			this.address = new AddressDTO(location.getAddress());
		}
		if (location.getCoordinate() != null) {
			this.coordinate = new CoordinateDTO(location.getCoordinate());
		}
	}

	@JsonIgnore
	public Location getApiModel() {
		Location location = new Location();
		if (address != null) {
			location.setAddress(address.getPersistenceModel());
		}
		if (coordinate != null) {
			location.setCoordinate(coordinate.getApiModel());
		}
		return location;
	}
}
