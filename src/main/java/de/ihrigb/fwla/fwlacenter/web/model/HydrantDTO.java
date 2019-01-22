package de.ihrigb.fwla.fwlacenter.web.model;

import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.ihrigb.fwla.fwlacenter.persistence.model.Hydrant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HydrantDTO {

	private String id;
	private LocationDTO location;
	private HydrantSignDTO sign;
	private Integer diameter;
	private String notes;

	public HydrantDTO(Hydrant hydrant) {
		Assert.notNull(hydrant, "Hydrant must not be null.");
		this.id = hydrant.getId();
		if (hydrant.getLocation() != null) {
			this.location = new LocationDTO(hydrant.getLocation());
		}
		if (hydrant.getSign() != null) {
			this.sign = new HydrantSignDTO(hydrant.getSign());
		}
		this.diameter = hydrant.getDiameter();
		this.notes = hydrant.getNotes();
	}

	@JsonIgnore
	public Hydrant getPersistenceModel() {
		Hydrant hydrant = new Hydrant();
		hydrant.setId(id);
		if (location != null) {
			hydrant.setLocation(location.getApiModel());
		}
		if (sign != null) {
			hydrant.setSign(sign.getPersistenceModel());
		}
		hydrant.setDiameter(diameter);
		hydrant.setNotes(notes);
		return hydrant;
	}
}
