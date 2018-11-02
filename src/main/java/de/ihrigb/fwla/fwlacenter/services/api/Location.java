package de.ihrigb.fwla.fwlacenter.services.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Location {
	private String street;
	private String town;
	private String district;
	private Coordinate coordinate;
}
