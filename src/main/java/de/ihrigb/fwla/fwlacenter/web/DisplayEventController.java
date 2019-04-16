package de.ihrigb.fwla.fwlacenter.web;

import java.time.Instant;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.ihrigb.fwla.fwlacenter.persistence.model.DisplayEvent;
import de.ihrigb.fwla.fwlacenter.persistence.repository.DisplayEventRepository;
import de.ihrigb.fwla.fwlacenter.web.exception.BadRequestException;
import de.ihrigb.fwla.fwlacenter.web.model.DisplayEventDTO;

@Transactional
@RestController
@RequestMapping("/v1/displayEvents")
public class DisplayEventController
		extends BaseController<DisplayEvent, String, DisplayEventDTO, DisplayEventRepository> {

	public DisplayEventController(DisplayEventRepository repository) {
		super(repository);
	}

	@GetMapping
	public ResponseEntity<?> getAll(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "filter", required = false) String filter,
			@RequestParam(name = "sort", required = false) String sort) {
		return super.doGetAll(page, size, filter, sort);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getOne(@PathVariable("id") String id) {
		return super.doGetOne(id);
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody DisplayEventDTO dto) {
		return super.doCreate(dto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable("id") String id, @RequestBody DisplayEventDTO dto) {
		return super.doUpdate(id, dto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") String id) {
		return super.doDelete(id);
	}

	@Override
	protected Function<? super DisplayEvent, ? extends DisplayEventDTO> getToDTOFunction() {
		return displayEvent -> {
			return new DisplayEventDTO(displayEvent);
		};
	}

	@Override
	protected Function<? super DisplayEventDTO, ? extends DisplayEvent> getFromDTOFunction() {
		return dto -> {
			return dto.getPersistenceModel();
		};
	}

	@Override
	protected String getId(DisplayEvent t) {
		return t.getId();
	}

	@Override
	protected void beforeCreate(DisplayEvent entity) {
		handleNullInstants(entity);
		validateDisplayEvent(entity);
		checkTimeRangeUniqueness(entity);
	}

	@Override
	protected void beforeUpdate(DisplayEvent entity) {
		handleNullInstants(entity);
		validateDisplayEvent(entity);
		checkTimeRangeUniqueness(entity);
	}

	private void handleNullInstants(DisplayEvent displayEvent) {
		if (displayEvent.getStartTime() == null) {
			displayEvent.setStartTime(Instant.MIN);
		}
		if (displayEvent.getEndTime() == null) {
			displayEvent.setEndTime(Instant.MAX);
		}
	}

	private void validateDisplayEvent(DisplayEvent displayEvent) {
		if (displayEvent.getStartTime().isAfter(displayEvent.getEndTime())) {
			throw new BadRequestException("Start time must be before end time.", Optional.of(displayEvent));
		}
	}

	private void checkTimeRangeUniqueness(DisplayEvent displayEvent) {
		if (!getRepository().getEventsIntersectingTimeRange(displayEvent.getStartTime(), displayEvent.getEndTime())
				.isEmpty()) {
			throw new BadRequestException("Time range does intersect with another display event.",
					Optional.of(displayEvent));
		}
	}
}
