package de.ihrigb.fwla.fwlacenter.api;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Address {

	@Column(name = "address_street")
	private String street;
	@Column(name = "address_zip")
	private String zip;
	@Column(name = "address_town")
	private String town;
	@Column(name = "address_district")
	private String district;
}
