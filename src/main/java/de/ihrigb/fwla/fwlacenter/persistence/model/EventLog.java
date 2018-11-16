package de.ihrigb.fwla.fwlacenter.persistence.model;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
@Table(name = "eventlogs")
public class EventLog {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(name = "id", nullable = false, unique = true)
	private String id;

	@Column(name = "time", nullable = false)
	private Instant time;

	@Column(name = "type", nullable = false)
	@Enumerated(EnumType.STRING)
	private EventLogType type;

	@Column(name = "message", nullable = false)
	private String message;

}
