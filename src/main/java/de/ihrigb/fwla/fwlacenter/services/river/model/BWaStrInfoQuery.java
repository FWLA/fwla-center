package de.ihrigb.fwla.fwlacenter.services.river.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class BWaStrInfoQuery extends Query {

	@JsonProperty("searchterm")
	private String searchTerm;

	@JsonProperty("searchfield")
	private SearchField searchField;

	public static enum SearchField {
		all, bwastrid, bwastr_name, strecken_name;
	}
}
