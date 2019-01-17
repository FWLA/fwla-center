package de.ihrigb.fwla.fwlacenter.web.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.ihrigb.fwla.fwlacenter.api.Coordinate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CoordinateDTO {

	private double latitude;
	private double longitude;

	public CoordinateDTO(Coordinate coordinate) {
		this.latitude = coordinate.getLatitude();
		this.longitude = coordinate.getLongitude();
	}

	@JsonIgnore
	public Coordinate getApiModel() {
		Coordinate coordinate = new Coordinate();
		coordinate.setLatitude(latitude);
		coordinate.setLongitude(longitude);
		return coordinate;
	}
}
