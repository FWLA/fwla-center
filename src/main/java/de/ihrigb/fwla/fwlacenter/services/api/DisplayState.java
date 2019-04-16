package de.ihrigb.fwla.fwlacenter.services.api;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import de.ihrigb.fwla.fwlacenter.api.Coordinate;
import de.ihrigb.fwla.fwlacenter.persistence.model.Operation;
import de.ihrigb.fwla.fwlacenter.services.api.Weather;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class DisplayState {
	private final State state;
	private final String serverVersion;
	@Builder.Default
	private Optional<Weather> weather = Optional.empty();
	@Builder.Default
	private Optional<Operation> operation = Optional.empty();
	@Builder.Default
	private Optional<String> text = Optional.empty();
	@Builder.Default
	private Optional<Coordinate> home = Optional.empty();

	public static enum State {
		IDLE, OPERATION, TEXT;
	}
}
