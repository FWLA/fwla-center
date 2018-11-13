package de.ihrigb.fwla.fwlacenter.web.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.ihrigb.fwla.fwlacenter.persistence.model.ActiveResource;
import de.ihrigb.fwla.fwlacenter.persistence.repository.StationRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ActiveResourceDTO extends ResourceDTO {

	public ActiveResourceDTO(ActiveResource activeResource) {
		super(activeResource);
	}

	@JsonIgnore
	public ActiveResource getPersistenceModel(StationRepository stationRepository) {
		ActiveResource activeResource = new ActiveResource();
		return populate(activeResource, stationRepository);
	}
}
