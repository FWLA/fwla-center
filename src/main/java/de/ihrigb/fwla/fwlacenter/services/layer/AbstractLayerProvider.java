package de.ihrigb.fwla.fwlacenter.services.layer;

import org.geojson.Feature;
import org.geojson.GeoJsonObject;

import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerProvider;

abstract class AbstractLayerProvider implements LayerProvider {

	final static String HASDETAILS_PROPERTY_KEY = "hasDetails";
	final static String COLOR_PROPERTY_KEY = "color";
	final static String NAME_PROPERTY_KEY =  "name";

	protected Feature createFeature(String id) {
		return createFeature(id, null);
	}

	protected Feature createFeature(String id, boolean hasDetails) {
		return createFeature(id, null, hasDetails);
	}

	protected Feature createFeature(String id, GeoJsonObject geometry) {
		return createFeature(id, geometry, false);
	}

	protected Feature createFeature(String id, GeoJsonObject geometry, boolean hasDetails) {
		Feature feature = new Feature();
		feature.setId(id);
		if (geometry != null) {
			feature.setGeometry(geometry);
		}
		setHasDetailsProperty(feature, hasDetails);
		return feature;
	}

	protected void setNameProperty(Feature feature, String name) {
		feature.setProperty(NAME_PROPERTY_KEY, name);
	}

	protected void setColorProperty(Feature feature, String color) {
		feature.setProperty(COLOR_PROPERTY_KEY, color);
	}

	protected void setHasDetailsProperty(Feature feature, boolean hasDetails) {
		feature.setProperty(HASDETAILS_PROPERTY_KEY, hasDetails);
	}
}
