package de.ihrigb.fwla.fwlacenter.web.model;

import de.ihrigb.fwla.fwlacenter.services.api.geo.Layer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LayerDTO {
	private String id;
	private String name;

	public LayerDTO(Layer layer) {
		this.id = layer.getId();
		this.name = layer.getName();
	}
}
