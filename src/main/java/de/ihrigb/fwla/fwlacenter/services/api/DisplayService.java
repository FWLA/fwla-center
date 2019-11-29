package de.ihrigb.fwla.fwlacenter.services.api;

import de.ihrigb.fwla.fwlacenter.persistence.model.Station;

/**
 * Service to manage display.
 */
public interface DisplayService {

	/**
	 * Get the current state of a dispaly.
	 *
	 * @param station station of the display
	 * @return current display state
	 */
	DisplayState getDisplayState(Station station);
}
