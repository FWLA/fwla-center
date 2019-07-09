package de.ihrigb.fwla.fwlacenter.services.api.geo;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class Feature {
	private String id;
	private String tooltip;
	private FeatureType type;
}
