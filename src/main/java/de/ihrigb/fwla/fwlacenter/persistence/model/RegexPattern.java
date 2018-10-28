package de.ihrigb.fwla.fwlacenter.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "regex_patterns")
@Getter
@Setter
public class RegexPattern {

	@Id
	@Column(name = "field_name", nullable = false, unique = true)
	private String fieldName;

	@Column(name = "pattern", nullable = false)
	private String pattern;

	@Column(name = "source", nullable = true)
	@Enumerated(EnumType.STRING)
	private Source source;
}
