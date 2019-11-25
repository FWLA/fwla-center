package de.ihrigb.fwla.fwlacenter.persistence.model;

import java.time.Instant;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import de.ihrigb.fwla.fwlacenter.api.Coordinate;
import de.ihrigb.fwla.fwlacenter.api.Locatable;
import de.ihrigb.fwla.fwlacenter.api.Location;
import lombok.Data;

@Data
@Entity
@Table(name = "roadblocks")
public class Roadblock implements Locatable {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(name = "id", nullable = false, unique = true)
	private String id;

	@Column(name = "start_time", nullable = false)
	private Instant startTime;

	@Column(name = "end_time", nullable = false)
	private Instant endTime;

	@Column(name = "information", nullable = true)
	private String information;

	@Embedded
	private Location location;

	@Override
	public Optional<Coordinate> locate() {
		return Optional.ofNullable(location).map(loc -> loc.getCoordinate());
	}
}
