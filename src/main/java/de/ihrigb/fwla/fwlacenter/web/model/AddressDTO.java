package de.ihrigb.fwla.fwlacenter.web.model;

import de.ihrigb.fwla.fwlacenter.persistence.model.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddressDTO {

	private String street;
	private String number;
	private String town;
	private String district;

	public AddressDTO(Address address) {
		this.street = address.getStreet();
		this.number = address.getNumber();
		this.town = address.getTown();
		this.district = address.getDistrict();
	}

	public Address create() {
		Address address = new Address();
		update(address);
		return address;
	}

	public void update(Address address) {
		address.setStreet(street);
		address.setNumber(number);
		address.setTown(town);
		address.setDistrict(district);
	}
}
