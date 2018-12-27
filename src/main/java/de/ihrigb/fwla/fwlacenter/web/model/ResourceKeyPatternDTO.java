package de.ihrigb.fwla.fwlacenter.web.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.ihrigb.fwla.fwlacenter.persistence.model.ResourceKeyPattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResourceKeyPatternDTO extends BasePatternDTO {

	public ResourceKeyPatternDTO(ResourceKeyPattern resourceKeyPattern) {
		super(resourceKeyPattern);
	}

	@JsonIgnore
	public ResourceKeyPattern getPersistenceModel() {
		ResourceKeyPattern resourceKeyPattern = new ResourceKeyPattern();
		super.populate(resourceKeyPattern);
		return resourceKeyPattern;
	}
}
