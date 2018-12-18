package de.ihrigb.fwla.fwlacenter.services.eventlog;

import java.time.Instant;

import org.springframework.stereotype.Component;

import de.ihrigb.fwla.fwlacenter.persistence.model.EventLog;
import de.ihrigb.fwla.fwlacenter.persistence.model.EventLogType;
import de.ihrigb.fwla.fwlacenter.persistence.repository.EventLogRepository;
import de.ihrigb.fwla.fwlacenter.services.api.EventLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventLogServiceImpl implements EventLogService {

	private final EventLogRepository repository;

	@Override
	public void info(String message) {
		createEventLog(EventLogType.INFO, message);
	}

	@Override
	public void error(String message) {
		createEventLog(EventLogType.ERROR, message);
	}

	private void createEventLog(EventLogType type, String message) {
		log.debug("Event log {}: {}.", type, message);

		EventLog eventLog = new EventLog();
		eventLog.setTime(Instant.now());
		eventLog.setMessage(message);
		eventLog.setType(type);

		repository.save(eventLog);
	}
}
