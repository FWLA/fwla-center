package de.ihrigb.fwla.fwlacenter.api;

import java.util.Optional;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Location implements Locatable {
	@Embedded
	private Address address;
	@Embedded
	private Coordinate coordinate;

	@Override
	public Optional<Coordinate> locate() {
		return Optional.ofNullable(coordinate);
	}

	public void clearEmptyStrings() {
		if (address != null) {
			address.clearEmptyStrings();
		}
	}
}
