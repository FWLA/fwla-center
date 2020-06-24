package de.ihrigb.fwla.fwlacenter.web.model;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.geojson.FeatureCollection;

import de.ihrigb.commons.Assert;
import de.ihrigb.fwla.fwlacenter.persistence.model.Operation;
import de.ihrigb.fwla.fwlacenter.persistence.model.Resource;
import de.ihrigb.fwla.fwlacenter.persistence.model.Station;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OperationDisplayDTO {
	private String id;
	private Instant time;
	private String place;
	private String object;
	private LocationDTO location;
	private String code;
	private String message;
	private String notice;
	private boolean isTraining = false;
	private boolean closed;
	private List<String> resourceKeys;
	private OperationKeyDTO operationKey;
	private RealEstateDTO realEstate;
	private String realEstateAdditional;
	private Set<String> stations;
	private boolean ambulanceCalled;
	private FeatureCollection directions;

	public OperationDisplayDTO(Operation operation) {
		this(operation, null);
	}

	public OperationDisplayDTO(Operation operation, Station station) {
		Assert.notNull(operation, "Operation must not be null.");

		this.id = operation.getId();
		this.time = operation.getTime();
		this.place = operation.getPlace();
		this.object = operation.getObject();
		if (operation.getLocation() != null) {
			this.location = new LocationDTO(operation.getLocation());
		}
		this.code = operation.getCode();
		this.message = operation.getMessage();
		this.notice = operation.getNotice();
		this.isTraining = operation.isTraining();
		this.closed = operation.isClosed();
		this.resourceKeys = operation.getResourceKeys();
		if (operation.getOperationKey() != null) {
			this.operationKey = new OperationKeyDTO(operation.getOperationKey());
		}
		if (operation.getRealEstate() != null) {
			this.realEstate = new RealEstateDTO(operation.getRealEstate());
		}
		this.realEstateAdditional = operation.getRealEstateAdditional();
		if (operation.getResources() != null) {
			this.stations = operation.getResources().stream().map(Resource::getStation).filter(Objects::nonNull)
					.filter(s -> {
						return station == null || !station.getId().equals(s.getId());
					}).map(Station::getName).filter(Objects::nonNull).collect(Collectors.toSet());
		}
		this.ambulanceCalled = operation.isAmbulanceCalled();
	}
}
