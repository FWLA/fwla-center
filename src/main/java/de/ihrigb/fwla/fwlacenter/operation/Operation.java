package de.ihrigb.fwla.fwlacenter.operation;

import java.time.Instant;

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
	private String operationType;
	private Boolean closed;
}
