package de.ihrigb.fwla.fwlacenter.web.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.ihrigb.fwla.fwlacenter.persistence.model.Station;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StationDTO {

	private String id;
	private String name;
	private LocationDTO location;

	public StationDTO(Station station) {
		this.id = station.getId();
		this.name = station.getName();
		if (station.getLocation() != null) {
			this.location = new LocationDTO(station.getLocation());
		}
	}

	@JsonIgnore
	public Station getPersistenceModel() {
		Station station = new Station();
		station.setId(id);
		station.setName(name);
		if (location != null) {
			station.setLocation(location.getApiModel());
		}
		return station;
	}
}
