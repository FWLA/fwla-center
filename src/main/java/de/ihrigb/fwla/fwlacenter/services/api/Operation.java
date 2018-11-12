package de.ihrigb.fwla.fwlacenter.services.api;

import java.time.Instant;
import java.util.List;

import de.ihrigb.fwla.fwlacenter.persistence.model.OperationKey;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Operation {
	private String id;
	private Instant time;
	private String place;
	private String object;
	private Location location;
	private String code;
	private String message;
	private String notice;
	private OperationKey operationKey;
	private boolean isTraining = false;
	private boolean closed;
	private List<String> resourceKeys;
}
