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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
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

	@Column(name = "number", nullable = true, unique = true)
	private String number;

	@Column(name = "information", nullable = true)
	private String information;

	@Embedded
	private Location location;

	@ElementCollection
	@CollectionTable(name = "real_estate_links", joinColumns = @JoinColumn(name = "real_estate_id"))
	private Set<Link> links;

	@Column(name = "folder_address", nullable = true)
	private Integer folderAddress;

	@ManyToMany
	@JoinTable(name = "real_estate_real_estate_tags", joinColumns = @JoinColumn(name = "real_estate_id"), inverseJoinColumns = @JoinColumn(name = "real_estate_tag_id"))
	private Set<RealEstateTag> realEstateTags;

	@Override
	public Optional<Coordinate> locate() {
		if (location == null) {
			return Optional.empty();
		}
		return this.location.locate();
	}

	@PrePersist
	@PostPersist
	public void clearEmptyStrings() {
		if ("".equals(id)) {
			id = null;
		}
		if ("".equals(name)) {
			name = null;
		}
		if ("".equals(pattern)) {
			pattern = null;
		}
		if ("".equals(number)) {
			number = null;
		}
		if ("".equals(information)) {
			information = null;
		}
		if (links != null) {
			links.forEach(link -> {
				link.clearEmptyStrings();
			});
		}
	}
}
