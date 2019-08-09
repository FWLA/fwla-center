package de.ihrigb.fwla.fwlacenter.services.api.geo;

import java.util.List;
import java.util.Optional;

import org.geojson.FeatureCollection;

public interface LayerProvider {

	/**
	 * Get all layers.
	 *
	 * @return
	 */
	List<LayerGroup> getLayerGroups();

	/**
	 * Get features for a specific layer.
	 *
	 * @param layerId layer id
	 * @return feature collection
	 */
	FeatureCollection getFeatures(String layerId);

	/**
	 * Get the details of a feature.
	 *
	 * @param layerId   layer id
	 * @param featureId feature id
	 * @return optional feature details
	 */
	Optional<FeatureDetails> getFeatureDetails(String layerId, String featureId);

	/**
	 * Check, if provider supports layer id.
	 * 
	 * @param layerId layer id
	 * @return true, if layer id is supported.
	 */
	boolean supports(String layerId);

	/**
	 * Check, if layer is editable.
	 *
	 * @param layerId layer id
	 * @return true, if layer can be edited
	 */
	boolean isEditable(String layerId);

	/**
	 * Update the layer by new geojson data.
	 *
	 * @param layerId           layer id
	 * @param featureCollection geojson feature collection
	 * @throws LayerUpdateNotSupportedException if layer does not support update
	 */
	void update(String layerId, FeatureCollection featureCollection) throws LayerUpdateNotSupportedException;
}
