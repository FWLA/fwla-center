package de.ihrigb.fwla.fwlacenter.services.river.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.geojson.Point;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeocodierungResult extends Result {

	@JsonProperty("bwastrid")
	private String bWaStrId;

	@JsonProperty("geometry")
	private Point geometry;
}
