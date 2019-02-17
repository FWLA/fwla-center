package de.ihrigb.fwla.fwlacenter.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@Entity
@Table(name = "river_sectors")
public class RiverSector {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(name = "id", nullable = false, unique = true)
	private String id;

	@Column(name = "river", nullable = false)
	@Enumerated(EnumType.STRING)
	private River river;
	
	@Column(name = "kmfrom", nullable = false)
	private int from;

	@Column(name = "kmto", nullable = false)
	private int to;

	@Column(name = "kminterval", nullable = false)
	private float interval;
}
