package de.ihrigb.fwla.fwlacenter.web.model;

import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.ihrigb.fwla.fwlacenter.persistence.model.Link;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LinkDTO {

	private String link;
	private String description;

	public LinkDTO(Link link) {
		Assert.notNull(link, "Link must not be null.");
		this.link = link.getLink();
		this.description = link.getDescription();
	}

	@JsonIgnore
	public Link getPersistenceModel() {
		Link link = new Link();
		link.setLink(this.link);
		link.setDescription(this.description);
		return link;
	}
}
