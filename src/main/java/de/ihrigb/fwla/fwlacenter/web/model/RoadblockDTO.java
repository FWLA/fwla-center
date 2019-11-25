package de.ihrigb.fwla.fwlacenter.web.model;

import java.time.Instant;

import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.ihrigb.fwla.fwlacenter.persistence.model.Roadblock;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoadblockDTO {

	private String id;
	private Instant startTime;
	private Instant endTime;
	private String information;
	private LocationDTO location;

	public RoadblockDTO(Roadblock roadblock) {
		Assert.notNull(roadblock, "Roadblock must not be null.");

		this.id = roadblock.getId();
		this.startTime = roadblock.getStartTime();
		this.endTime = roadblock.getEndTime();
		this.information = roadblock.getInformation();
		if (roadblock.getLocation() != null) {
			this.location = new LocationDTO(roadblock.getLocation());
		}
	}

	@JsonIgnore
	public Roadblock getPersistenceModel() {
		Roadblock roadblock = new Roadblock();
		roadblock.setId(id);
		roadblock.setStartTime(startTime);
		roadblock.setEndTime(endTime);
		roadblock.setInformation(information);
		if (location != null) {
			roadblock.setLocation(location.getApiModel());
		}
		return roadblock;
	}
}
