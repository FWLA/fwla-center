package de.ihrigb.fwla.fwlacenter.web.model;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.geojson.FeatureCollection;
import org.springframework.util.Assert;

import de.ihrigb.fwla.fwlacenter.persistence.repository.RealEstateTagRepository;
import de.ihrigb.fwla.fwlacenter.persistence.repository.StationRepository;
import de.ihrigb.fwla.fwlacenter.persistence.model.Operation;
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
	private List<ResourceDTO> resources;
	private boolean ambulanceCalled;
	private FeatureCollection directions;

	public OperationDisplayDTO(Operation operation) {
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
			this.resources = operation.getResources().stream().map(r -> new ResourceDTO(r)).collect(Collectors.toList());
		}
		this.ambulanceCalled = operation.isAmbulanceCalled();
		this.directions = operation.getDirections();
	}

	@JsonIgnore
	public Operation getApiModel(StationRepository stationRepository, RealEstateTagRepository realEstateTagRepository) {
		Operation operation = new Operation();
		operation.setId(id);
		operation.setTime(time);
		operation.setPlace(place);
		operation.setObject(object);
		if (location != null) {
			operation.setLocation(location.getApiModel());
		}
		operation.setCode(code);
		operation.setMessage(message);
		operation.setNotice(notice);
		operation.setTraining(isTraining);
		operation.setClosed(closed);
		operation.setResourceKeys(resourceKeys);
		if (operationKey != null) {
			operation.setOperationKey(operationKey.getPersistenceModel());
		}
		if (realEstate != null) {
			operation.setRealEstate(realEstate.getPersistenceModel(realEstateTagRepository));
		}
		operation.setRealEstateAdditional(realEstateAdditional);
		if (resources != null) {
			operation.setResources(resources.stream().map(dto -> dto.getPersistenceModel(stationRepository))
					.collect(Collectors.toList()));
		}
		operation.setAmbulanceCalled(ambulanceCalled);
		operation.setDirections(directions);
		return operation;
	}
}
