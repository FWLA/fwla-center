package de.ihrigb.fwla.fwlacenter.web.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.ihrigb.fwla.fwlacenter.persistence.model.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddressDTO {

	private String street;
	private String zip;
	private String town;
	private String district;

	public AddressDTO(Address address) {
		this.street = address.getStreet();
		this.zip = address.getZip();
		this.town = address.getTown();
		this.district = address.getDistrict();
	}

	@JsonIgnore
	public Address getPersistenceModel() {
		Address address = new Address();
		address.setStreet(street);
		address.setZip(zip);
		address.setTown(town);
		address.setDistrict(district);
		return address;
	}
}
