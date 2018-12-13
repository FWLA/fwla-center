package de.ihrigb.fwla.fwlacenter.services.api;

import java.util.Optional;

/**
 * Represents the state of a display.
 */
public interface DisplayState {

	/**
	 * Get the basic state of the display.
	 *
	 * @return state of display
	 */
	State getState();

	/**
	 * Optionally includes weather information.
	 *
	 * @return optional of weather
	 */
	Optional<Weather> getWeather();

	/**
	 * Optionally includes operation information.
	 *
	 * @return optional of operation
	 */
	Optional<Operation> getOperation();

	/**
	 * Optionally provides text to be displayed.
	 *
	 * @return optional of text (html)
	 */
	Optional<String> getText();

	public static enum State {
		IDLE, OPERATION, TEXT;
	}
}
