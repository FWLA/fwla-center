package de.ihrigb.fwla.fwlacenter.web.model;

import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.ihrigb.commons.Assert;
import de.ihrigb.fwla.fwlacenter.persistence.model.RealEstate;
import de.ihrigb.fwla.fwlacenter.persistence.repository.RealEstateTagRepository;
import de.ihrigb.fwla.fwlacenter.web.ReferenceNotFoundException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RealEstateDTO {

	private String id;
	private String name;
	private String pattern;
	private String number;
	private String information;
	private LocationDTO location;
	private Set<LinkDTO> links;
	private Integer folderAddress;
	private Set<String> realEstateTags;

	public RealEstateDTO(RealEstate realEstate) {
		Assert.notNull(realEstate, "RealEstate must not be null.");

		this.id = realEstate.getId();
		this.name = realEstate.getName();
		this.pattern = realEstate.getPattern();
		this.number = realEstate.getNumber();
		this.information = realEstate.getInformation();
		if (realEstate.getLocation() != null) {
			this.location = new LocationDTO(realEstate.getLocation());
		}
		if (realEstate.getLinks() != null) {
			this.links = realEstate.getLinks().stream().map(link -> new LinkDTO(link)).collect(Collectors.toSet());
		}
		this.folderAddress = realEstate.getFolderAddress();
		if (realEstate.getRealEstateTags() != null) {
			this.realEstateTags = realEstate.getRealEstateTags().stream().map(ret -> ret.getId())
					.collect(Collectors.toSet());
		}
	}

	@JsonIgnore
	public RealEstate getPersistenceModel(RealEstateTagRepository realEstateTagRepository) {
		RealEstate realEstate = new RealEstate();
		realEstate.setId(id);
		realEstate.setName(name);
		realEstate.setPattern(pattern);
		realEstate.setNumber(number);
		realEstate.setInformation(information);
		if (location != null) {
			realEstate.setLocation(location.getApiModel());
		}
		if (links != null) {
			realEstate.setLinks(links.stream().map(link -> link.getPersistenceModel()).collect(Collectors.toSet()));
		}
		realEstate.setFolderAddress(folderAddress);
		if (realEstateTags != null) {
			realEstate.setRealEstateTags(realEstateTags.stream()
					.map(realEstateTagId -> realEstateTagRepository.findById(realEstateTagId)
							.orElseThrow(() -> new ReferenceNotFoundException("resource -> station")))
					.collect(Collectors.toSet()));
		}
		return realEstate;
	}
}
