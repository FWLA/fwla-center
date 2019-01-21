package de.ihrigb.fwla.fwlacenter.services.layer;

import java.util.Comparator;

import de.ihrigb.fwla.fwlacenter.services.api.geo.Layer;

public class LayerComparator implements Comparator<Layer> {

	static LayerComparator INSTANCE = new LayerComparator();

	private LayerComparator() {
	}

	@Override
	public int compare(Layer o1, Layer o2) {
		return String.CASE_INSENSITIVE_ORDER.compare(o1.getName(), o2.getName());
	}
}
