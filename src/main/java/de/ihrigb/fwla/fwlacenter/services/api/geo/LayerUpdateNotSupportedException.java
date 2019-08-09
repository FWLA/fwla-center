package de.ihrigb.fwla.fwlacenter.services.api.geo;

public class LayerUpdateNotSupportedException extends Exception {
	private final static long serialVersionUID = 1L;

	public LayerUpdateNotSupportedException(String layerId) {
		super(String.format("Update not supported for layer %s.", layerId));
	}
}
