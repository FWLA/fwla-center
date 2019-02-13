package de.ihrigb.fwla.fwlacenter.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
@Table(name = "operation_keys")
public class OperationKey {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(name = "id", nullable = false, unique = true)
	private String id;

	@Column(name = "key", nullable = false, unique = true)
	private String key;

	@Column(name = "code", nullable = true, unique = true)
	private String code;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	private OperationType type;

	@Column(name = "danger_to_life", nullable = false)
	private boolean dangerToLife;

	@PrePersist
	@PostPersist
	public void clearEmptyStrings() {
		if ("".equals(id)) {
			id = null;
		}
		if ("".equals(key)) {
			key = null;
		}
		if ("".equals(code)) {
			code = null;
		}
	}
}
