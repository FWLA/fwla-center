package de.ihrigb.fwla.fwlacenter.services.api.geo;

import de.ihrigb.fwla.fwlacenter.persistence.model.MapLayerCategory;

public enum LayerGroupCategory {
	OPERATION, INFO, ARCHIVE;

	public static LayerGroupCategory of(MapLayerCategory category) {
		if (category == null) {
			return null;
		}
		return LayerGroupCategory.valueOf(category.name());
	}
}
