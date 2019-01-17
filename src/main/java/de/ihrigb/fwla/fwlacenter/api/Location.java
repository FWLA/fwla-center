package de.ihrigb.fwla.fwlacenter.api;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import de.ihrigb.fwla.fwlacenter.api.Coordinate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Location {
	@Embedded
	private Address address;
	@Embedded
	private Coordinate coordinate;
}
