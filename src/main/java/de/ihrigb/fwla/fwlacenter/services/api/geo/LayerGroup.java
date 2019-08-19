package de.ihrigb.fwla.fwlacenter.services.api.geo;

import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LayerGroup implements Comparable<LayerGroup> {

	private String name;
	private List<Layer> layers;
	private LayerGroupCategory category;

	@Override
	public int compareTo(LayerGroup o) {
		return name.compareTo(o.name);
	}

	public void sortLayers() {
		Collections.sort(layers);
	}
}
