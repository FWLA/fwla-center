package de.ihrigb.fwla.fwlacenter.web.model;

import de.ihrigb.fwla.fwlacenter.services.api.geo.Feature;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FeatureDTO {

	private String name;
	private String text;

	public FeatureDTO(Feature feature) {
		this.name = feature.getName();
		this.text = feature.getText();
	}
}
