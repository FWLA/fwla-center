package de.ihrigb.fwla.fwlacenter.services.river.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Fehlkilometer {

	@JsonProperty("km_von")
	private float kmVon;

	@JsonProperty("km_bis")
	private float kmBis;
}
