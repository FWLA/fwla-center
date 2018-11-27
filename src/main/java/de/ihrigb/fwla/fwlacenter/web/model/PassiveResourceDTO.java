package de.ihrigb.fwla.fwlacenter.web.model;

import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.ihrigb.fwla.fwlacenter.persistence.model.PassiveResource;
import de.ihrigb.fwla.fwlacenter.persistence.repository.ActiveResourceRepository;
import de.ihrigb.fwla.fwlacenter.persistence.repository.StationRepository;
import de.ihrigb.fwla.fwlacenter.web.ReferenceNotFoundException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PassiveResourceDTO extends ResourceDTO {

	private Set<String> attachableToIds;

	public PassiveResourceDTO(PassiveResource passiveResource) {
		super(passiveResource);
		if (passiveResource.getAttachableTo() != null) {
			this.attachableToIds = passiveResource.getAttachableTo().stream().map(r -> r.getId())
					.collect(Collectors.toSet());
		}
	}

	@JsonIgnore
	public PassiveResource getPersistenceModel(StationRepository stationRepository,
			ActiveResourceRepository activeResourceRepository) {
		PassiveResource passiveResource = new PassiveResource();
		if (this.attachableToIds != null) {
			passiveResource.setAttachableTo(this.attachableToIds.stream().map(id -> {
				return activeResourceRepository.findById(id)
						.orElseThrow(() -> new ReferenceNotFoundException("PassiveResource -> ActiveResource"));
			}).collect(Collectors.toSet()));
		}
		return super.populate(passiveResource, stationRepository);
	}
}
