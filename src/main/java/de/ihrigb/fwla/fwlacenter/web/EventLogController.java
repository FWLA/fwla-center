package de.ihrigb.fwla.fwlacenter.web;

import java.util.function.Function;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.ihrigb.fwla.fwlacenter.persistence.model.EventLog;
import de.ihrigb.fwla.fwlacenter.persistence.repository.EventLogRepository;
import de.ihrigb.fwla.fwlacenter.web.model.EventLogDTO;

@Transactional
@RestController
@RequestMapping("/v1/eventLogs")
public class EventLogController extends BaseController<EventLog, String, EventLogDTO> {

	public EventLogController(EventLogRepository repository) {
		super(repository);
	}

	@GetMapping
	public ResponseEntity<?> getAll(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "filter", required = false) String filter,
			@RequestParam(name = "sort", required = false) String sort) {
		return super.doGetAll(page, size, filter, sort);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") String id) {
		return super.doDelete(id);
	}

	@Override
	protected Function<? super EventLogDTO, ? extends EventLog> getFromDTOFunction() {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Function<? super EventLog, ? extends EventLogDTO> getToDTOFunction() {
		return eventLog -> {
			return new EventLogDTO(eventLog);
		};
	}

	@Override
	protected String getId(EventLog t) {
		return t.getId();
	}
}
