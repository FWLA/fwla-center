package de.ihrigb.fwla.fwlacenter.web.model;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.ihrigb.commons.Assert;
import de.ihrigb.fwla.fwlacenter.persistence.model.DisplayEvent;
import de.ihrigb.fwla.fwlacenter.persistence.repository.StationRepository;
import de.ihrigb.fwla.fwlacenter.web.ReferenceNotFoundException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DisplayEventDTO {

	private String id;
	private Instant startTime;
	private Instant endTime;
	private String text;
	private boolean showOperation;
	private String stationId;

	public DisplayEventDTO(DisplayEvent displayEvent) {
		Assert.notNull(displayEvent, "DisplayEvent must not be null.");

		this.id = displayEvent.getId();
		this.startTime = displayEvent.getStartTime();
		this.endTime = displayEvent.getEndTime();
		this.text = displayEvent.getText();
		this.showOperation = displayEvent.isShowOperation();
		if (displayEvent.getStation() != null) {
			this.stationId = displayEvent.getStation().getId();
		}
	}

	@JsonIgnore
	public DisplayEvent getPersistenceModel(StationRepository stationRepository) {
		DisplayEvent displayEvent = new DisplayEvent();
		displayEvent.setId(id);
		displayEvent.setStartTime(startTime);
		displayEvent.setEndTime(endTime);
		displayEvent.setText(text);
		displayEvent.setShowOperation(showOperation);
		if (stationId != null) {
			displayEvent.setStation(stationRepository.findById(stationId)
					.orElseThrow(() -> new ReferenceNotFoundException("displayEvent -> station")));
		}
		return displayEvent;
	}
}
