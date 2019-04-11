package de.ihrigb.fwla.fwlacenter.persistence.model;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
@Table(name = "display_events")
public class DisplayEvent {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(name = "id", nullable = false, unique = true)
	private String id;

	@Column(name = "start_time", nullable = true)
	private Instant startTime;

	@Column(name = "end_time", nullable = true)
	private Instant endTime;

	@Lob
	@Column(name = "text", nullable = true)
	private String text;

	@Column(name = "show_operation", nullable = false)
	private boolean showOperation = true;
}
