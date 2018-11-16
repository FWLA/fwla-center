package de.ihrigb.fwla.fwlacenter.web.model;

import java.time.Instant;

import de.ihrigb.fwla.fwlacenter.persistence.model.EventLog;
import de.ihrigb.fwla.fwlacenter.persistence.model.EventLogType;
import lombok.Getter;

@Getter
public class EventLogDTO {

	private String id;
	private Instant time;
	private EventLogType type;
	private String message;

	public EventLogDTO(EventLog eventLog) {
		this.id = eventLog.getId();
		this.time = eventLog.getTime();
		this.type = eventLog.getType();
		this.message = eventLog.getMessage();
	}
}
