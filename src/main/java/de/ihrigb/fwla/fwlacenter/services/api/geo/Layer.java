package de.ihrigb.fwla.fwlacenter.services.api.geo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Layer implements Comparable<Layer> {

	private String id;
	private String name;
	private boolean editable;

	public Layer(String id, String name) {
		this(id, name, false);
	}

	@Override
	public int compareTo(Layer o) {
		return name.compareTo(o.name);
	}
}
