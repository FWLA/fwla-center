package de.ihrigb.fwla.fwlacenter.services.api.geo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Feature {
	private String type;
	private String name;
	private String text;
	private String color;
}
