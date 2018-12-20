package de.ihrigb.fwla.fwlacenter.persistence.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@MappedSuperclass
public abstract class Resource {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(name = "id", nullable = false, unique = true)
	private String id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "key", nullable = true)
	private String key;

	@Column(name = "radio", nullable = true, unique = true)
	private String radio;

	@ManyToOne(optional = false)
	@JoinColumn(name = "station_id", referencedColumnName = "id")
	private Station station;

	@Column(name = "in_service", nullable = false)
	private boolean inService = Boolean.TRUE;
}
