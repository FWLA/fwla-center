package de.ihrigb.fwla.fwlacenter.services.river.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RootResult<T extends Result> {

	@JsonProperty("result")
	private List<T> result;
}
