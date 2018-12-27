package de.ihrigb.fwla.fwlacenter.web.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.ihrigb.fwla.fwlacenter.persistence.model.BasePattern;
import de.ihrigb.fwla.fwlacenter.persistence.model.PatternMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class BasePatternDTO {

	private String id;
	private String pattern;
	private PatternMode mode;

	public BasePatternDTO(BasePattern basePattern) {
		this.id = basePattern.getId();
		this.pattern = basePattern.getPattern();
		this.mode = basePattern.getMode();
	}

	@JsonIgnore
	protected void populate(BasePattern basePattern) {
		basePattern.setId(id);
		basePattern.setPattern(pattern);
		basePattern.setMode(mode);
	}
}
