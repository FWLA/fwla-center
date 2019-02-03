package de.ihrigb.fwla.fwlacenter.mail.receive;

import de.ihrigb.fwla.fwlacenter.utils.StringUtils;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class MailFilter {

	private final ReceivingProperties properties;

	FilterResult filter(String sender) {
		if (properties.getWhitelistHot() != null && !properties.getWhitelistHot().isEmpty()) {
			if (StringUtils.containsIgnoreCase(properties.getWhitelistHot(), sender)) {
				return FilterResult.HOT;
			}
		}

		if (properties.getWhitelistTraining() != null && !properties.getWhitelistTraining().isEmpty()) {
			if (StringUtils.containsIgnoreCase(properties.getWhitelistTraining(), sender)) {
				return FilterResult.TRAINING;
			}
		}

		return FilterResult.REJECTED;
	}

	static enum FilterResult {
		HOT, TRAINING, REJECTED;
	}
}
