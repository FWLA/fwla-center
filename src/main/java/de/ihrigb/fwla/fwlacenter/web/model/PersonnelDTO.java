package de.ihrigb.fwla.fwlacenter.web.model;

import java.util.Optional;

import org.springframework.util.Assert;

import de.ihrigb.fwla.fwlacenter.persistence.model.Personnel;
import de.ihrigb.fwla.fwlacenter.persistence.repository.StationRepository;
import de.ihrigb.fwla.fwlacenter.web.ReferenceNotFoundException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PersonnelDTO {

	private String id;
	private String name;
	private String stationId;
	private LocationDTO home;
	private LocationDTO work;

	public PersonnelDTO(Personnel personnel) {
		Assert.notNull(personnel, "Personnel must not be null.");
		this.id = personnel.getId();
		this.name = personnel.getName();
		if (personnel.getStation() != null) {
			this.stationId = personnel.getStation().getId();
		}
		personnel.getHome().ifPresent(home -> {
			this.home = new LocationDTO(home);
		});
		personnel.getWork().ifPresent(work -> {
			this.work = new LocationDTO(work);
		});
	}

	public Personnel getPersistenceModel(StationRepository stationRepository) {
		Personnel personnel = new Personnel();
		personnel.setId(id);
		personnel.setName(name);
		if (stationId != null) {
			personnel.setStation(stationRepository.findById(stationId)
					.orElseThrow(() -> new ReferenceNotFoundException("personnel -> station")));
		}
		personnel.setHome(Optional.ofNullable(home).map(h -> h.getApiModel()));
		personnel.setWork(Optional.ofNullable(work).map(w -> w.getApiModel()));
		return personnel;
	}
}
