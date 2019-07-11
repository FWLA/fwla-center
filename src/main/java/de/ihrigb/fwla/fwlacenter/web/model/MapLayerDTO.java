package de.ihrigb.fwla.fwlacenter.web.model;

import org.geojson.FeatureCollection;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.ihrigb.fwla.fwlacenter.persistence.model.MapLayer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MapLayerDTO {

	private String id;
	private String name;
	private FeatureCollection geoJson;

	public MapLayerDTO(MapLayer mapLayer) {
		Assert.notNull(mapLayer, "MapLayer must not be null.");

		this.id = mapLayer.getId();
		this.name = mapLayer.getName();
		this.geoJson = mapLayer.getGeoJson();
	}

	@JsonIgnore
	public MapLayer getPersistenceModel() {
		MapLayer mapLayer = new MapLayer();
		mapLayer.setId(id);
		mapLayer.setName(name);
		mapLayer.setGeoJson(geoJson);
		return mapLayer;
	}
}
