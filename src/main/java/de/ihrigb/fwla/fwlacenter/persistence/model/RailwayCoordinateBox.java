package de.ihrigb.fwla.fwlacenter.persistence.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import de.ihrigb.fwla.fwlacenter.api.Coordinate;
import lombok.Data;

@Data
@Entity
@Table(name = "railway_coordinate_boxes")
public class RailwayCoordinateBox {


	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(name = "id", nullable = false, unique = true)
	private String id;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "latitude", column = @Column(name = "ul_latitude", nullable = false)),
		@AttributeOverride(name = "longitude", column = @Column(name = "ul_longitude", nullable = false))
	})
	private Coordinate upperLeft;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "latitude", column = @Column(name = "lr_latitude", nullable = false)),
		@AttributeOverride(name = "longitude", column = @Column(name = "lr_longitude", nullable = false))
	})
	private Coordinate lowerRight;
}
