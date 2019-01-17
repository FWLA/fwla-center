package de.ihrigb.fwla.fwlacenter.api;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public final class Coordinate implements Locatable {

	@Column(name = "latitude")
	private double latitude;
	@Column(name = "longitude")
	private double longitude;

	@Override
	public Optional<Coordinate> locate() {
		return Optional.of(this);
	}
}
