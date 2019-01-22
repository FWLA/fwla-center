package de.ihrigb.fwla.fwlacenter.persistence.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import de.ihrigb.fwla.fwlacenter.api.Location;
import lombok.Data;

@Data
@Entity
@Table(name = "hydrants")
public class Hydrant {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(name = "id", nullable = false, unique = true)
	private String id;

	@Embedded
	private Location location;

	@Embedded
	private HydrantSign sign;

	@Column(name = "diameter", nullable = true)
	private Integer diameter;

	@Lob
	@Column(name = "notes", nullable = true)
	private String notes;
}
