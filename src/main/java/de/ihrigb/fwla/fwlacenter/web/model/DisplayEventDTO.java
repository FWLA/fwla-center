package de.ihrigb.fwla.fwlacenter.web.model;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.util.Assert;

import de.ihrigb.fwla.fwlacenter.persistence.model.DisplayEvent;
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

	public DisplayEventDTO(DisplayEvent displayEvent) {
		Assert.notNull(displayEvent, "DisplayEvent must not be null.");

		this.id = displayEvent.getId();
		this.startTime = displayEvent.getStartTime();
		this.endTime = displayEvent.getEndTime();
		this.text = displayEvent.getText();
		this.showOperation = displayEvent.isShowOperation();
	}

	@JsonIgnore
	public DisplayEvent getPersistenceModel() {
		DisplayEvent displayEvent = new DisplayEvent();
		displayEvent.setId(id);
		displayEvent.setStartTime(startTime);
		displayEvent.setEndTime(endTime);
		displayEvent.setText(text);
		displayEvent.setShowOperation(showOperation);
		return displayEvent;
	}
}
