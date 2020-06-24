package de.ihrigb.fwla.fwlacenter.web.model;

import org.geojson.FeatureCollection;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.ihrigb.commons.Assert;
import de.ihrigb.fwla.fwlacenter.persistence.model.MapLayer;
import de.ihrigb.fwla.fwlacenter.persistence.model.MapLayerCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MapLayerDTO {

	private String id;
	private String name;
	private MapLayerCategory category;
	private FeatureCollection geoJson;

	public MapLayerDTO(MapLayer mapLayer) {
		Assert.notNull(mapLayer, "MapLayer must not be null.");

		this.id = mapLayer.getId();
		this.name = mapLayer.getName();
		this.category = mapLayer.getCategory();
		this.geoJson = mapLayer.getGeoJson();
	}

	@JsonIgnore
	public MapLayer getPersistenceModel() {
		MapLayer mapLayer = new MapLayer();
		mapLayer.setId(id);
		mapLayer.setName(name);
		mapLayer.setCategory(category);
		mapLayer.setGeoJson(geoJson);
		return mapLayer;
	}
}
