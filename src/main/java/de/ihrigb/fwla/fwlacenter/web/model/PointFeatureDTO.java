package de.ihrigb.fwla.fwlacenter.web.model;

import de.ihrigb.fwla.fwlacenter.services.api.geo.PointFeature;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PointFeatureDTO extends FeatureDTO {

	private CoordinateDTO coordinate;
	private String color;

	public PointFeatureDTO(PointFeature pointFeature) {
		super(pointFeature);
		if (pointFeature.getCoordinate() != null) {
			this.coordinate = new CoordinateDTO(pointFeature.getCoordinate());
		}
		this.color = pointFeature.getColor();
	}
}
