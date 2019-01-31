package de.ihrigb.fwla.fwlacenter.web.model;

import java.util.Set;

import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.ihrigb.fwla.fwlacenter.persistence.model.RealEstate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RealEstateDTO {

	private String id;
	private String name;
	private String pattern;
	private String information;
	private LocationDTO location;
	private Set<String> links;
	private Integer folderAddress;

	public RealEstateDTO(RealEstate realEstate) {
		Assert.notNull(realEstate, "RealEstate must not be null.");

		this.id = realEstate.getId();
		this.name = realEstate.getName();
		this.pattern = realEstate.getPattern();
		this.information = realEstate.getInformation();
		if (realEstate.getLocation() != null) {
			this.location = new LocationDTO(realEstate.getLocation());
		}
		this.links = realEstate.getLinks();
		this.folderAddress = realEstate.getFolderAddress();
	}

	@JsonIgnore
	public RealEstate getPersistenceModel() {
		RealEstate realEstate = new RealEstate();
		realEstate.setId(id);
		realEstate.setName(name);
		realEstate.setPattern(pattern);
		realEstate.setInformation(information);
		if (location != null) {
			realEstate.setLocation(location.getApiModel());
		}
		realEstate.setLinks(links);
		realEstate.setFolderAddress(folderAddress);
		return realEstate;
	}
}
