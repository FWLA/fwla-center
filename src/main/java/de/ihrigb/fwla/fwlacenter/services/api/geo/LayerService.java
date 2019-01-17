package de.ihrigb.fwla.fwlacenter.services.api.geo;

import java.util.Set;

public interface LayerService {

	/**
	 * Get all layers.
	 *
	 * @return
	 */
	Set<Layer> getLayers();

	/**
	 * Get all features
	 *
	 * @return
	 */
	Set<? extends Feature> getFeatures();

	/**
	 * Get features for a specific layer.
	 *
	 * @param layer
	 * @return
	 */
	Set<? extends Feature> getFeatures(String layer);
}
