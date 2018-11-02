package de.ihrigb.fwla.fwlacenter.display;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import de.ihrigb.fwla.fwlacenter.services.api.Weather;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
@JsonInclude(Include.NON_NULL)
public class BaseDisplayState {
	private final String state;
	@Setter
	private Weather weather;
}
