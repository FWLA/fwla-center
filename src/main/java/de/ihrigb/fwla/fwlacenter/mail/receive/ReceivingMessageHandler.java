package de.ihrigb.fwla.fwlacenter.mail.receive;

import java.io.IOException;
import java.time.Instant;

import javax.mail.Address;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import de.ihrigb.fwla.fwlacenter.handling.api.OperationChain;
import de.ihrigb.fwla.fwlacenter.mail.Email;
import de.ihrigb.fwla.fwlacenter.mail.api.MailExtractionService;
import de.ihrigb.fwla.fwlacenter.services.api.Operation;
import de.ihrigb.fwla.fwlacenter.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ReceivingMessageHandler implements MessageHandler {

	private final ReceivingProperties properties;
	private final MailExtractionService mailExtractionService;
	private final OperationChain operationChain;

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		try {
			MimeMessage mimeMessage = (MimeMessage) message.getPayload();

			Address[] addresses = mimeMessage.getFrom();
			String sender = ((InternetAddress) addresses[0]).getAddress();
			if (addresses != null && filterSender(sender)) {
				String subject = mimeMessage.getSubject();
				String text = getText(mimeMessage);
				Instant timestamp = Instant.ofEpochMilli(message.getHeaders().getTimestamp());

				Email email = new Email(sender, subject, text, timestamp);
				Operation operation = mailExtractionService.extract(email);
				operationChain.put(operation);
			}
		} catch (Exception e) {
			log.error("Exception while handling incomming message.", e);
		}
	}

	private boolean filterSender(String sender) {
		if (properties.getWhitelist() != null && !properties.getWhitelist().isEmpty()) {
			if (!StringUtils.containsIgnoreCase(properties.getWhitelist(), sender)) {
				return false;
			}
		}

		if (properties.getBlacklist() != null && !properties.getBlacklist().isEmpty()) {
			if (StringUtils.containsIgnoreCase(properties.getBlacklist(), sender)) {
				return false;
			}
		}

		return true;
	}

	private String getText(Part p) throws javax.mail.MessagingException, IOException {
		if (p.isMimeType("text/*")) {
			Object content = p.getContent();
			return content == null ? null : content.toString();
		}

		if (p.isMimeType("multipart/alternative")) {
			// Prefer html text over plain text
			Multipart mp = (Multipart) p.getContent();
			String text = null;
			for (int i = 0; i < mp.getCount(); i++) {
				Part bp = mp.getBodyPart(i);
				if (bp.isMimeType("text/plain")) {
					if (text == null) {
						text = getText(bp);
					}
					continue;
				} else if (bp.isMimeType("text/html")) {
					String s = getText(bp);
					if (s != null) {
						return s;
					}
				} else {
					return getText(bp);
				}
			}
			return text;
		} else if (p.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) p.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				String s = getText(mp.getBodyPart(i));
				if (s != null) {
					return s;
				}
			}
		}

		return null;
	}
}
