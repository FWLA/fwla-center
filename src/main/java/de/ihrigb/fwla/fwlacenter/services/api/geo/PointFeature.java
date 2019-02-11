package de.ihrigb.fwla.fwlacenter.services.api.geo;

import de.ihrigb.fwla.fwlacenter.api.Coordinate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PointFeature extends Feature {
	private Coordinate coordinate;
	private String color;

	public PointFeature(String id, Coordinate coordinate, String color) {
		super(id, FeatureType.POINT);
		this.coordinate = coordinate;
		this.color = color;
	}
}
