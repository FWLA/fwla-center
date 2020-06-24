package de.ihrigb.fwla.fwlacenter.web.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.ihrigb.commons.Assert;
import de.ihrigb.fwla.fwlacenter.persistence.model.RailwayCoordinateBox;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RailwayCoordinateBoxDTO {

	private String id;
	private CoordinateDTO upperLeft;
	private CoordinateDTO lowerRight;
	
	public RailwayCoordinateBoxDTO(RailwayCoordinateBox coordinateBox) {
		Assert.notNull(coordinateBox, "RailwayCoordinateBox must not be null.");
		this.id = coordinateBox.getId();
		if (coordinateBox.getUpperLeft() != null) {
			this.upperLeft = new CoordinateDTO(coordinateBox.getUpperLeft());
		}
		if (coordinateBox.getLowerRight() != null) {
			this.lowerRight = new CoordinateDTO(coordinateBox.getLowerRight());
		}
	}

	@JsonIgnore
	public RailwayCoordinateBox getPersistenceModel() {
		RailwayCoordinateBox coordinateBox = new RailwayCoordinateBox();
		coordinateBox.setId(this.id);
		if (this.upperLeft != null) {
			coordinateBox.setUpperLeft(this.upperLeft.getApiModel());
		}
		if (this.lowerRight != null) {
			coordinateBox.setLowerRight(this.lowerRight.getApiModel());
		}
		return coordinateBox;
	}
}
