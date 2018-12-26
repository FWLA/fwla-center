package de.ihrigb.fwla.fwlacenter.web.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.ihrigb.fwla.fwlacenter.persistence.model.PatternMode;
import de.ihrigb.fwla.fwlacenter.persistence.model.ResourceKeyPattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResourceKeyPatternDTO {

	private String id;
	private String pattern;
	private PatternMode mode;

	public ResourceKeyPatternDTO(ResourceKeyPattern resourceKeyPattern) {
		this.id = resourceKeyPattern.getId();
		this.pattern = resourceKeyPattern.getPattern();
		this.mode = resourceKeyPattern.getMode();
	}

	@JsonIgnore
	public ResourceKeyPattern getPersistenceModel() {
		ResourceKeyPattern resourceKeyPattern = new ResourceKeyPattern();
		resourceKeyPattern.setId(id);
		resourceKeyPattern.setPattern(pattern);
		resourceKeyPattern.setMode(mode);
		return resourceKeyPattern;
	}
}
