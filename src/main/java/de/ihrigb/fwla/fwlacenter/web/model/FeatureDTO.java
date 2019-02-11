package de.ihrigb.fwla.fwlacenter.web.model;

import de.ihrigb.fwla.fwlacenter.services.api.geo.Feature;
import de.ihrigb.fwla.fwlacenter.services.api.geo.FeatureType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FeatureDTO {

	private String id;
	private FeatureType type;

	public FeatureDTO(Feature feature) {
		this.id = feature.getId();
		this.type = feature.getType();
	}
}
