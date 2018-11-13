package de.ihrigb.fwla.fwlacenter.web.model;

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
	private String key;
	private String information;
	private AddressDTO address;

	public RealEstateDTO(RealEstate realEstate) {
		this.id = realEstate.getId();
		this.name = realEstate.getName();
		this.key = realEstate.getKey();
		this.information = realEstate.getInformation();
		if (realEstate.getAddress() != null) {
			this.address = new AddressDTO(realEstate.getAddress());
		}
	}

	public RealEstate getPersistenceModel() {
		RealEstate realEstate = new RealEstate();
		realEstate.setId(id);
		realEstate.setName(name);
		realEstate.setKey(key);
		realEstate.setInformation(information);
		if (address != null) {
			realEstate.setAddress(address.getPersistenceModel());
		}
		return realEstate;
	}
}
