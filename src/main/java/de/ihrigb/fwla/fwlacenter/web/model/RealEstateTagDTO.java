package de.ihrigb.fwla.fwlacenter.web.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.ihrigb.commons.Assert;
import de.ihrigb.fwla.fwlacenter.persistence.model.RealEstateTag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RealEstateTagDTO {

	private String id;
	private String name;

	public RealEstateTagDTO(RealEstateTag realEstateTag) {
		Assert.notNull(realEstateTag, "RealEstateTag must not be null.");
		this.id = realEstateTag.getId();
		this.name = realEstateTag.getName();
	}

	@JsonIgnore
	public RealEstateTag getPersistenceModel() {
		RealEstateTag realEstateTag = new RealEstateTag();
		realEstateTag.setId(id);
		realEstateTag.setName(name);
		return realEstateTag;
	}
}

