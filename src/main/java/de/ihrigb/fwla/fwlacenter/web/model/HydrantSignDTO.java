package de.ihrigb.fwla.fwlacenter.web.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.ihrigb.fwla.fwlacenter.persistence.model.HydrantSign;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HydrantSignDTO {

	private float l;
	private float r;
	private float c;

	public HydrantSignDTO(HydrantSign hydrantSign) {
		this.l = hydrantSign.getL();
		this.r = hydrantSign.getR();
		this.c = hydrantSign.getC();
	}

	@JsonIgnore
	public HydrantSign getPersistenceModel() {
		HydrantSign hydrantSign = new HydrantSign();
		hydrantSign.setL(l);
		hydrantSign.setR(r);
		hydrantSign.setC(c);
		return hydrantSign;
	}
}
