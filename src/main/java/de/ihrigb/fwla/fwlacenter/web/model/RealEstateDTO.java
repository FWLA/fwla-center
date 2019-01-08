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
	private AddressDTO address;
	private Set<String> links;

	public RealEstateDTO(RealEstate realEstate) {
		Assert.notNull(realEstate, "RealEstate must not be null.");

		this.id = realEstate.getId();
		this.name = realEstate.getName();
		this.pattern = realEstate.getPattern();
		this.information = realEstate.getInformation();
		if (realEstate.getAddress() != null) {
			this.address = new AddressDTO(realEstate.getAddress());
		}
		this.links = realEstate.getLinks();
	}

	@JsonIgnore
	public RealEstate getPersistenceModel() {
		RealEstate realEstate = new RealEstate();
		realEstate.setId(id);
		realEstate.setName(name);
		realEstate.setPattern(pattern);
		realEstate.setInformation(information);
		if (address != null) {
			realEstate.setAddress(address.getPersistenceModel());
		}
		realEstate.setLinks(links);
		return realEstate;
	}
}
