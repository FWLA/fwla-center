package de.ihrigb.fwla.fwlacenter.mail.receive;

import de.ihrigb.fwla.fwlacenter.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
class MailFilter {

	private final ReceivingProperties properties;

	FilterResult filter(String sender) {
		if (properties.getWhitelistHot() != null && !properties.getWhitelistHot().isEmpty()) {
			if (StringUtils.containsIgnoreCase(properties.getWhitelistHot(), sender)) {
				log.debug("Mail from '{}' filtered as 'HOT'.", sender);
				return FilterResult.HOT;
			}
		}

		if (properties.getWhitelistTraining() != null && !properties.getWhitelistTraining().isEmpty()) {
			if (StringUtils.containsIgnoreCase(properties.getWhitelistTraining(), sender)) {
				log.debug("Mail from '{}' filtered as 'TRAINING'.", sender);
				return FilterResult.TRAINING;
			}
		}

		log.debug("Mail from '{}' filtered as 'REJECTED'.", sender);
		return FilterResult.REJECTED;
	}

	static enum FilterResult {
		HOT, TRAINING, REJECTED;
	}
}
