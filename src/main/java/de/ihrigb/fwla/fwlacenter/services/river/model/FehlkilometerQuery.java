package de.ihrigb.fwla.fwlacenter.services.river.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FehlkilometerQuery extends Query {

	@JsonProperty("bwastrid")
	private String bWaStrId;

	@JsonProperty("km_von")
	private Float kmVon;

	@JsonProperty("km_bis")
	private Float kmBis;
}
