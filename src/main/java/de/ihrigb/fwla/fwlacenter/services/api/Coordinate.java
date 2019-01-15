package de.ihrigb.fwla.fwlacenter.services.api;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public final class Coordinate {

	@Column(name = "latitude")
	private double latitude;
	@Column(name = "longitude")
	private double longitude;
}
