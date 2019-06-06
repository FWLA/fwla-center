package de.ihrigb.fwla.fwlacenter.persistence.model;

import java.util.Optional;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import de.ihrigb.fwla.fwlacenter.api.Location;
import lombok.Data;

@Data
@Entity
@Table(name = "personnel")
public class Personnel {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(name = "id", nullable = false, unique = true)
	private String id;
	
	@Column(name = "name", nullable = false)
	private String name;

	@ManyToOne
	@JoinColumn(name = "station_id")
	private Station station;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "address.street", column = @Column(name = "home_address_street")),
		@AttributeOverride(name = "address.zip", column = @Column(name = "home_address_zip")),
		@AttributeOverride(name = "address.town", column = @Column(name = "home_address_town")),
		@AttributeOverride(name = "address.district", column = @Column(name = "home_address_district")),
		@AttributeOverride(name = "coordinate.latitude", column = @Column(name = "home_latitude")),
		@AttributeOverride(name = "coordinate.longitude", column = @Column(name = "home_longitude"))
	})
	private Optional<Location> home;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "address.street", column = @Column(name = "work_address_street")),
		@AttributeOverride(name = "address.zip", column = @Column(name = "work_address_zip")),
		@AttributeOverride(name = "address.town", column = @Column(name = "work_address_town")),
		@AttributeOverride(name = "address.district", column = @Column(name = "work_address_district")),
		@AttributeOverride(name = "coordinate.latitude", column = @Column(name = "work_latitude")),
		@AttributeOverride(name = "coordinate.longitude", column = @Column(name = "work_longitude"))
	})
	private Optional<Location> work;
}
