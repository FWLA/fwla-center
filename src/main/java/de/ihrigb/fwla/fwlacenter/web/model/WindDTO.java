package de.ihrigb.fwla.fwlacenter.web.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import de.ihrigb.commons.Assert;
import de.ihrigb.fwla.fwlacenter.services.api.Wind;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class WindDTO {

	private float speed;
	private int degrees;

	public WindDTO(Wind wind) {
		Assert.notNull(wind, "Wind must not be null.");
		this.speed = wind.getSpeed();
		this.degrees = wind.getDegrees();
	}
}
