package de.ihrigb.fwla.fwlacenter.services.river.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RootQuery<T extends Query> {

	@JsonProperty("queries")
	private List<T> queries;
}
