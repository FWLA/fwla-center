package de.ihrigb.fwla.fwlacenter.web.model;

import org.springframework.util.Assert;

import de.ihrigb.fwla.fwlacenter.services.api.geo.FeatureDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FeatureDetailsDTO {

	private String name;
	private String text;
	private AddressDTO address;

	public FeatureDetailsDTO(FeatureDetails featureDetails) {
		Assert.notNull(featureDetails, "FeatureDetails must not be null.");
		this.name = featureDetails.getName();
		this.text = featureDetails.getText();
		if (featureDetails.getAddress() != null) {
			this.address = new AddressDTO(featureDetails.getAddress());
		}
	}
}
