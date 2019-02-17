package de.ihrigb.fwla.fwlacenter.services.river.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
abstract public class Result {

	@JsonProperty("qid")
	private int qid;
}
