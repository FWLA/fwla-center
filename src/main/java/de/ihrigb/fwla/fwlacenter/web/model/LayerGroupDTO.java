package de.ihrigb.fwla.fwlacenter.web.model;

import java.util.List;
import java.util.stream.Collectors;

import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerGroup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LayerGroupDTO {
	private String name;
	private List<LayerDTO> layers;

	public LayerGroupDTO(LayerGroup layerGroup) {
		this.name = layerGroup.getName();
		if (layerGroup.getLayers() != null) {
			this.layers = layerGroup.getLayers().stream().map(layer -> new LayerDTO(layer)).collect(Collectors.toList());
		}
	}
}
