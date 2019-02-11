package de.ihrigb.fwla.fwlacenter.services.api.geo;

import de.ihrigb.fwla.fwlacenter.api.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeatureDetails {
	private String name;
	private String text;
	private Address address;
}
