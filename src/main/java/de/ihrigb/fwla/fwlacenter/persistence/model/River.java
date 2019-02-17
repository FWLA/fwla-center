package de.ihrigb.fwla.fwlacenter.persistence.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum River {

	RHINE("Rhein", "3901"), NECKAR("Neckar", "3301");

	public static River ofBWaStrId(String bWaStrId) {
		for (River river : River.values()) {
			if (bWaStrId.equals(river.getBWaStrId())) {
				return river;
			}
		}
		return null;
	}

	private final String name;
	private final String bWaStrId;
}
