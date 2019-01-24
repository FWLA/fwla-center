package de.ihrigb.fwla.fwlacenter.services.api.geo;

import java.util.List;

import de.ihrigb.fwla.fwlacenter.api.Coordinate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PolyLineFeature extends Feature {
	private List<Coordinate> coordinates;
	private String color;

	public PolyLineFeature(String name, String text, String color, List<Coordinate> coordiantes) {
		super("polyline", name, text, color);
		this.coordinates = coordiantes;
	}
}
