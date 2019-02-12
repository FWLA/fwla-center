package de.ihrigb.fwla.fwlacenter.services.api.geo;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface LayerService {

	/**
	 * Get all layers.
	 *
	 * @return
	 */
	List<LayerGroup> getLayerGroups();

	/**
	 * Get features for a specific layer.
	 *
	 * @param layer
	 * @return
	 */
	Set<? extends Feature> getFeatures(String layer);

	/**
	 * Get the details of a feature.
	 *
	 * @param layer
	 * @param featureId
	 * @return
	 */
	Optional<FeatureDetails> getFeatureDetails(String layer, String featureId);
}
