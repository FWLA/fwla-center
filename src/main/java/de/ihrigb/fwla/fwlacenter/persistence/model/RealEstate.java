package de.ihrigb.fwla.fwlacenter.persistence.model;

import java.util.Optional;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import de.ihrigb.fwla.fwlacenter.api.Coordinate;
import de.ihrigb.fwla.fwlacenter.api.Locatable;
import de.ihrigb.fwla.fwlacenter.api.Location;
import lombok.Data;

@Data
@Entity
@Table(name = "real_estates")
public class RealEstate implements Locatable {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(name = "id", nullable = false, unique = true)
	private String id;

	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@Column(name = "key", nullable = true, unique = true)
	private String pattern;

	@Column(name = "information", nullable = true)
	private String information;

	@Embedded
	private Location location;

	@ElementCollection
	@CollectionTable(name = "real_estate_links", joinColumns = @JoinColumn(name = "real_estate_id"))
	@Column(name = "link", nullable = false)
	private Set<String> links;

	@Column(name="folder_address", nullable = true)
	private Integer folderAddress;

	@Override
	public Optional<Coordinate> locate() {
		if (location == null) {
			return Optional.empty();
		}
		return this.location.locate();
	}
}
