package de.ihrigb.fwla.fwlacenter.web.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DirectionsRequestDTO {
	private CoordinateDTO from;
	private CoordinateDTO to;
}
