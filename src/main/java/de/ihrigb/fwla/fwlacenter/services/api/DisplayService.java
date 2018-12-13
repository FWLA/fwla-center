package de.ihrigb.fwla.fwlacenter.services.api;

/**
 * Service to manage display.
 */
public interface DisplayService {

	/**
	 * Get the current state of a dispaly.
	 *
	 * @return current display state
	 */
	DisplayState getDisplayState();
}
