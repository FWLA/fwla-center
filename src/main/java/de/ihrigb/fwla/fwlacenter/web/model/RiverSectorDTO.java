package de.ihrigb.fwla.fwlacenter.web.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.ihrigb.commons.Assert;
import de.ihrigb.fwla.fwlacenter.persistence.model.River;
import de.ihrigb.fwla.fwlacenter.persistence.model.RiverSector;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RiverSectorDTO {

	private String id;
	private River river;
	private int from;
	private int to;
	private float interval;

	public RiverSectorDTO(RiverSector riverSector) {
		Assert.notNull(riverSector, "RiverSector must not be null.");
		this.id = riverSector.getId();
		this.river = riverSector.getRiver();
		this.from = riverSector.getFrom();
		this.to = riverSector.getTo();
		this.interval = riverSector.getInterval();
	}

	@JsonIgnore
	public RiverSector getPersistenceModel() {
		RiverSector riverSector = new RiverSector();
		riverSector.setId(id);
		riverSector.setRiver(river);
		riverSector.setFrom(from);
		riverSector.setTo(to);
		riverSector.setInterval(interval);
		return riverSector;
	}
}
