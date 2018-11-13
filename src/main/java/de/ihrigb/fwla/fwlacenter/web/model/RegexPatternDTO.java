package de.ihrigb.fwla.fwlacenter.web.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.ihrigb.fwla.fwlacenter.persistence.model.RegexPattern;
import de.ihrigb.fwla.fwlacenter.persistence.model.Source;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegexPatternDTO {

	private String id;
	private String pattern;
	private Source source;

	public RegexPatternDTO(RegexPattern regexPattern) {
		this.id = regexPattern.getId();
		this.pattern = regexPattern.getPattern();
		this.source = regexPattern.getSource();
	}

	@JsonIgnore
	public RegexPattern getPersistenceModel() {
		RegexPattern regexPattern = new RegexPattern();
		if (id != null) {
			regexPattern.setId(id);
		}
		regexPattern.setPattern(pattern);
		regexPattern.setSource(source);
		return regexPattern;
	}
}
