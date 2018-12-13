package de.ihrigb.fwla.fwlacenter.services.display;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import de.ihrigb.fwla.fwlacenter.services.api.DisplayState;
import de.ihrigb.fwla.fwlacenter.services.api.Operation;
import de.ihrigb.fwla.fwlacenter.services.api.Weather;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(Include.NON_NULL)
public class BaseDisplayState implements DisplayState {
	private final State state;
	@Builder.Default
	private Optional<Weather> weather = Optional.empty();
	@Builder.Default
	private Optional<Operation> operation = Optional.empty();
	@Builder.Default
	private Optional<String> text = Optional.empty();
}
