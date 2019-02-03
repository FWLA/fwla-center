package de.ihrigb.fwla.fwlacenter.services.api.geo;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LayerGroup {

	private String name;
	private Set<Layer> layers;
}
