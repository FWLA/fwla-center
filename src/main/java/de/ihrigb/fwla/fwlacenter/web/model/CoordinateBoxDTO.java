package de.ihrigb.fwla.fwlacenter.web.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoordinateBoxDTO {
	private CoordinateDTO sw;
	private CoordinateDTO ne;
}
