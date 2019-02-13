package de.ihrigb.fwla.fwlacenter.persistence.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Link {

	@Column(name = "link", nullable = false)
	private String link;

	@Column(name = "description", nullable = false)
	private String description;

	public void clearEmptyStrings() {
		if ("".equals(link)) {
			link = null;
		}
		if ("".equals(description)) {
			description = null;
		}
	}
}
