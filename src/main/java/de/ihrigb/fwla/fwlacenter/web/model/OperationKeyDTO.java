package de.ihrigb.fwla.fwlacenter.web.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.ihrigb.fwla.fwlacenter.persistence.model.OperationKey;
import de.ihrigb.fwla.fwlacenter.persistence.model.OperationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OperationKeyDTO {

	private String id;
	private String key;
	private String code;
	private OperationType type;
	private boolean dangerToLife;

	public OperationKeyDTO(OperationKey operationKey) {
		this.id = operationKey.getId();
		this.key = operationKey.getKey();
		this.code = operationKey.getCode();
		this.type = operationKey.getType();
		this.dangerToLife = operationKey.isDangerToLife();
	}

	@JsonIgnore
	public OperationKey getPersistenceModel() {
		OperationKey operationKey = new OperationKey();
		operationKey.setId(id);
		operationKey.setKey(key);
		operationKey.setCode(code);
		operationKey.setType(type);
		operationKey.setDangerToLife(dangerToLife);
		return operationKey;
	}
}
