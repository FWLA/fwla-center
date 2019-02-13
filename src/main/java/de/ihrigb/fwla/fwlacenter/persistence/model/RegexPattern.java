package de.ihrigb.fwla.fwlacenter.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "regex_patterns")
@Getter
@Setter
public class RegexPattern {

	@Id
	@Column(name = "id", nullable = false, unique = true)
	private String id;

	@Column(name = "pattern", nullable = false)
	private String pattern;

	@Column(name = "source", nullable = true)
	@Enumerated(EnumType.STRING)
	private Source source;

	@PrePersist
	@PostPersist
	public void clearEmptyStrings() {
		if ("".equals(id)) {
			id = null;
		}
		if ("".equals(pattern)) {
			pattern = null;
		}
	}
}
