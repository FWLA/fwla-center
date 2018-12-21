package de.ihrigb.fwla.fwlacenter.services.api;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import de.ihrigb.fwla.fwlacenter.persistence.model.OperationKey;
import de.ihrigb.fwla.fwlacenter.persistence.model.RealEstate;
import de.ihrigb.fwla.fwlacenter.persistence.model.Resource;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Operation {

	/*
	 * Data from email.
	 */
	private String id;
	private Instant time;
	private String place;
	private String object;
	private Location location;
	private String code;
	private String message;
	private String notice;
	private List<String> resourceKeys;

	/*
	 * Added information based on persistent data.
	 */
	private OperationKey operationKey;
	private RealEstate realEstate;
	private Set<Resource> resources;

	/*
	 * Metadata
	 */
	private Instant created = Instant.now();
	private boolean isTraining = false;
	private boolean closed;
}
