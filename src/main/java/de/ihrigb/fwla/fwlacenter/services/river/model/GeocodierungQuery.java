package de.ihrigb.fwla.fwlacenter.services.river.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class GeocodierungQuery extends Query {

	@JsonProperty("bwastrid")
	private String bWaStrId;

	@JsonProperty("stationierung")
	private Stationierung stationierung;

	@JsonProperty("spatialReference")
	private SpatialReference spatialReference = new SpatialReference();

	@Getter
	@Setter
	public static class Stationierung {

		@JsonProperty("km_wert")
		private float kmWert;
	
		@JsonProperty("offset")
		private float offset;
	}

	@Getter
	@Setter
	public static class SpatialReference {

		public static final int WGS84 = 4326;

		@JsonProperty("wkid")
		private int wkid = SpatialReference.WGS84;
	}
}
