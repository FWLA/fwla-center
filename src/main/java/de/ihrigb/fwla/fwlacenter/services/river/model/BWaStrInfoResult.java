package de.ihrigb.fwla.fwlacenter.services.river.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BWaStrInfoResult extends Result {

	@JsonProperty("bwastrid")
	private String bWaStrId;

	@JsonProperty("bwastr_name")
	private String bWaStrName;

	@JsonProperty("km_von")
	private float kmVon;

	@JsonProperty("km_bis")
	private float kmBis;

	@JsonProperty("fehlkilometer")
	private List<Fehlkilometer> fehlkilometer;
}
