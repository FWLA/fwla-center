package de.ihrigb.fwla.fwlacenter.mail.receive;

import org.springframework.transaction.annotation.Transactional;

import de.ihrigb.fwla.fwlacenter.handling.api.OperationChain;
import de.ihrigb.fwla.fwlacenter.mail.api.MailExtractionService;
import de.ihrigb.fwla.fwlacenter.persistence.model.Operation;
import de.ihrigb.fwla.mail.Email;
import de.ihrigb.fwla.mail.EmailHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class OperationEmailHandler implements EmailHandler<String> {

	private final ReceivingProperties properties;
	private final MailExtractionService mailExtractionService;
	private final OperationChain operationChain;

	@Transactional
	@Override
	public void handle(Email<String> email) {
		try {

				String sender = email.getSender();
				boolean isTraining;
				switch (new MailFilter(properties).filterInternal(sender)) {
				case REJECTED:
					log.info("Mail from '{}' was rejected.", sender);
					return;
				case TRAINING:
					log.info("Mail from '{}' classified as training.", sender);
					isTraining = true;
					break;
				case HOT:
				default:
					log.info("Mail from '{}' classified as hot.", sender);
					isTraining = false;
					break;
				}

				Operation operation = mailExtractionService.extract(email);
				if (isTraining) {
					operation.setTraining(true);
					operation.setId("t-" + operation.getId());
				}
				operationChain.put(operation);
		} catch (Exception e) {
			log.error("Exception while handling incomming message.", e);
		}
	}
}
