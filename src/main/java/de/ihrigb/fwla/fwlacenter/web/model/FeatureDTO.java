package de.ihrigb.fwla.fwlacenter.web.model;

import de.ihrigb.fwla.fwlacenter.services.api.geo.Feature;
import de.ihrigb.fwla.fwlacenter.services.api.geo.FeatureType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @deprecated 0.1.4
 */
@Deprecated
@Getter
@Setter
@NoArgsConstructor
public class FeatureDTO {

	private String id;
	private String tooltip;
	private FeatureType type;

	public FeatureDTO(Feature feature) {
		this.id = feature.getId();
		this.tooltip = feature.getTooltip();
		this.type = feature.getType();
	}
}
