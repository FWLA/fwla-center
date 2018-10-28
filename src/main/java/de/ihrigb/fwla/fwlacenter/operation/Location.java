package de.ihrigb.fwla.fwlacenter.operation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Location {
	private String street;
	private String town;
	private String district;
	private Double latitude;
	private Double longitude;
}
