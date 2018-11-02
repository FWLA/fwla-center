package de.ihrigb.fwla.fwlacenter.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "operation_keys")
public class OperationKey {

	@Id
	@Column(name = "key", nullable = false, unique = true)
	private String key;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	private OperationType type;
}
