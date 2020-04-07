package de.ihrigb.fwla.fwlacenter.mail.receive;

import de.ihrigb.fwla.fwlacenter.utils.StringUtils;
import de.ihrigb.fwla.mail.EmailSenderFilter;
import de.ihrigb.fwla.mail.FilterResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
class MailFilter implements EmailSenderFilter {

	private final ReceivingProperties properties;

	@Override
	public FilterResult filter(String sender) {
		switch (filterInternal(sender)) {
			case HOT:
			case TRAINING:
				return FilterResult.ACCEPTED;
			case REJECTED:
			default:
				return FilterResult.REJECTED;
		}
	}

	InternalFilterResult filterInternal(String sender) {
		if (properties.getWhitelistHot() != null && !properties.getWhitelistHot().isEmpty()) {
			if (StringUtils.containsIgnoreCase(properties.getWhitelistHot(), sender)) {
				log.debug("Mail from '{}' filtered as 'HOT'.", sender);
				return InternalFilterResult.HOT;
			}
		}

		if (properties.getWhitelistTraining() != null && !properties.getWhitelistTraining().isEmpty()) {
			if (StringUtils.containsIgnoreCase(properties.getWhitelistTraining(), sender)) {
				log.debug("Mail from '{}' filtered as 'TRAINING'.", sender);
				return InternalFilterResult.TRAINING;
			}
		}

		log.debug("Mail from '{}' filtered as 'REJECTED'.", sender);
		return InternalFilterResult.REJECTED;
	}

	static enum InternalFilterResult {
		HOT, TRAINING, REJECTED;
	}
}
