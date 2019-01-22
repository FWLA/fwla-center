package de.ihrigb.fwla.fwlacenter.persistence.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class HydrantSign {

	@Column(name = "hydrant_sign_l", nullable = true)
	private float l;

	@Column(name = "hydrant_sign_r", nullable = true)
	private float r;

	@Column(name = "hydrant_sign_c", nullable = true)
	private float c;
}
