package de.ihrigb.fwla.fwlacenter.web.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.ihrigb.fwla.fwlacenter.persistence.model.Resource;
import de.ihrigb.fwla.fwlacenter.persistence.repository.StationRepository;
import de.ihrigb.fwla.fwlacenter.web.ReferenceNotFoundException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResourceDTO {

	private String id;
	private String name;
	private String key;
	private String radio;
	private String stationId;
	private boolean inService;

	public ResourceDTO(Resource resource) {
		this.id = resource.getId();
		this.name = resource.getName();
		this.key = resource.getKey();
		this.radio = resource.getRadio();
		this.inService = resource.isInService();
		if (resource.getStation() != null) {
			this.stationId = resource.getStation().getId();
		}
	}

	@JsonIgnore
	public Resource getPersistenceModel(StationRepository stationRepository) {
		Resource resource = new Resource();
		resource.setId(id);
		resource.setName(name);
		resource.setKey(key);
		resource.setRadio(radio);
		resource.setInService(inService);
		if (stationId != null) {
			resource.setStation(stationRepository.findById(stationId)
					.orElseThrow(() -> new ReferenceNotFoundException("resource -> station")));
		}
		return resource;
	}
}
