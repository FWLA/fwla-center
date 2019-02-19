package de.ihrigb.fwla.fwlacenter.persistence.model;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.geojson.FeatureCollection;

import de.ihrigb.fwla.fwlacenter.api.Location;
import lombok.Data;

@Data
@Entity
@Table(name = "operations")
public class Operation {

	/*
	 * Data from email.
	 */
	@Id
	@Column(name = "id", nullable = false, unique = true)
	private String id;

	@Column(name = "time", nullable = true)
	private Instant time;

	@Column(name = "place", nullable = true)
	private String place;

	@Column(name = "object", nullable = true)
	private String object;

	@Embedded
	private Location location;

	@Column(name = "code", nullable = true)
	private String code;

	@Column(name = "message", nullable = true)
	private String message;

	@Column(name = "notice", nullable = true)
	private String notice;

	@ElementCollection
	@CollectionTable(name = "operation_resource_keys", joinColumns = @JoinColumn(name = "operation_id"))
	@OrderColumn(name = "index")
	@Column(name = "resource_key", nullable = false)
	private List<String> resourceKeys;

	/*
	 * Added information based on persistent data.
	 */
	@ManyToOne
	@JoinColumn(name = "operation_key_id", nullable = true)
	private OperationKey operationKey;

	@ManyToOne
	@JoinColumn(name = "real_estate_id", nullable = true)
	private RealEstate realEstate;

	@ManyToMany
	@JoinTable(name = "operation_resources", joinColumns = @JoinColumn(name = "operation_id"), inverseJoinColumns = @JoinColumn(name = "resource_id"))
	@OrderColumn(name = "index")
	private List<Resource> resources;

	@Column(name = "ambulance_called", nullable = false)
	private boolean ambulanceCalled = false;

	@Lob
	@Column(name = "directions", nullable = true)
	private FeatureCollection directions;

	/*
	 * Metadata
	 */
	@Column(name = "created", nullable = true)
	private Instant created = Instant.now();

	@Transient
	private boolean isTraining = false;

	@Column(name = "closed", nullable = false)
	private boolean closed = false;

	@Column(name = "year", nullable = false)
	private int year;

	@PrePersist
	public void prePersist() {
		clearEmptyStrings();
		setYear();
	}

	@PreUpdate
	public void preUpdate() {
		clearEmptyStrings();
		setYear();
	}

	public void setYear() {
		Instant source;
		if (time != null) {
			source = time;
		} else {
			source = created;
		}

		ZoneId zoneId = ZoneId.of("Europe/Berlin");
		ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(source, zoneId);

		this.year = zonedDateTime.getYear();
	}

	public void clearEmptyStrings() {
		if ("".equals(id)) {
			id = null;
		}
		if ("".equals(place)) {
			place = null;
		}
		if ("".equals(object)) {
			object = null;
		}
		if (location != null) {
			location.clearEmptyStrings();
		}
		if ("".equals(code)) {
			code = null;
		}
		if ("".equals(message)) {
			message = null;
		}
		if ("".equals(notice)) {
			notice = null;
		}
		if (resourceKeys != null) {
			resourceKeys.removeIf(resourceKey -> {
				return resourceKey == null || "".equals(resourceKey);
			});
		}
	}
}
