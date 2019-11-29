package de.ihrigb.fwla.fwlacenter.web.model;

import org.springframework.util.Assert;

import de.ihrigb.fwla.fwlacenter.persistence.model.Roadblock;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BasicRoadblockDTO {

	private String id;
	private CoordinateDTO coordinate;

	public BasicRoadblockDTO(Roadblock roadblock) {
		Assert.notNull(roadblock, "Roadblock must not be null.");

		this.id = roadblock.getId();
		if (roadblock.getLocation() != null && roadblock.getLocation().getCoordinate() != null) {
			this.coordinate = new CoordinateDTO(roadblock.getLocation().getCoordinate());
		}
	}
}
