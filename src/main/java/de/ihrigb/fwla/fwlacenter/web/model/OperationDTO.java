package de.ihrigb.fwla.fwlacenter.web.model;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import de.ihrigb.commons.Assert;
import de.ihrigb.fwla.fwlacenter.persistence.model.Operation;
import de.ihrigb.fwla.fwlacenter.persistence.repository.OperationKeyRepository;
import de.ihrigb.fwla.fwlacenter.persistence.repository.RealEstateRepository;
import de.ihrigb.fwla.fwlacenter.persistence.repository.ResourceRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OperationDTO {
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
	private String operationKeyId;
	private String realEstateId;
	private String realEstateAdditional;
	private List<String> resources;
	private boolean ambulanceCalled;

	public OperationDTO(Operation operation) {
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
			this.operationKeyId = operation.getOperationKey().getId();
		}
		if (operation.getRealEstate() != null) {
			this.realEstateId = operation.getRealEstate().getId();
		}
		this.realEstateAdditional = operation.getRealEstateAdditional();
		if (operation.getResources() != null) {
			this.resources = operation.getResources().stream().map(r -> r.getId()).collect(Collectors.toList());
		}
		this.ambulanceCalled = operation.isAmbulanceCalled();
	}

	public Operation getPersistenceModel(OperationKeyRepository operationKeyRepository,
			RealEstateRepository realEstateRepository, ResourceRepository resourceRepository) {
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
		if (operationKeyId != null) {
			operation.setOperationKey(operationKeyRepository.findById(operationKeyId).orElse(null));
		}
		if (realEstateId != null) {
			operation.setRealEstate(realEstateRepository.findById(realEstateId).orElse(null));
		}
		operation.setRealEstateAdditional(realEstateAdditional);
		if (resources != null) {
			operation.setResources(
					resources.stream().map(resourceId -> resourceRepository.findById(resourceId).orElse(null))
							.filter(Objects::nonNull).collect(Collectors.toList()));
		}
		operation.setAmbulanceCalled(ambulanceCalled);

		return operation;
	}
}
