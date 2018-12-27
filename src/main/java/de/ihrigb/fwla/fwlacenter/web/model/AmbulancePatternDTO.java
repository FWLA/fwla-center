package de.ihrigb.fwla.fwlacenter.web.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.ihrigb.fwla.fwlacenter.persistence.model.AmbulancePattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AmbulancePatternDTO extends BasePatternDTO {

	public AmbulancePatternDTO(AmbulancePattern AmbulancePattern) {
		super(AmbulancePattern);
	}

	@JsonIgnore
	public AmbulancePattern getPersistenceModel() {
		AmbulancePattern AmbulancePattern = new AmbulancePattern();
		super.populate(AmbulancePattern);
		return AmbulancePattern;
	}
}
