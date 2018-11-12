package de.ihrigb.fwla.fwlacenter.mail.receive;

import java.nio.charset.StandardCharsets;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.mail.ImapIdleChannelAdapter;
import org.springframework.integration.mail.ImapMailReceiver;
import org.springframework.messaging.MessageHandler;
import org.springframework.web.util.UriUtils;

import de.ihrigb.fwla.fwlacenter.handling.api.OperationChain;
import de.ihrigb.fwla.fwlacenter.mail.api.MailExtractionService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableIntegration
@ConditionalOnProperty("mail.receive.host")
@EnableConfigurationProperties(ReceivingProperties.class)
public class MailConfiguration {

	@Bean
	public ImapMailReceiver imapMailReceiver(ReceivingProperties properties) {
		String userInfo = String.format("%s:%s", properties.getUsername(), properties.getPassword());

		String url = String.format("imap://%s@%s:143/inbox", UriUtils.encodeUserInfo(userInfo, StandardCharsets.UTF_8),
				UriUtils.encodeHost(properties.getHost(), StandardCharsets.UTF_8));

		log.info("Connecting to {}.", url);
		ImapMailReceiver imapMailReceiver = new ImapMailReceiver(url);
		imapMailReceiver.setShouldMarkMessagesAsRead(Boolean.TRUE);
		return imapMailReceiver;
	}

	@Bean
	public ImapIdleChannelAdapter imapIdleChannelAdapter(DirectChannel directChannel,
			ImapMailReceiver imapMailReceiver) {
		ImapIdleChannelAdapter imapIdleChannelAdapter = new ImapIdleChannelAdapter(imapMailReceiver);
		imapIdleChannelAdapter.setOutputChannel(directChannel);
		imapIdleChannelAdapter.setAutoStartup(true);
		return imapIdleChannelAdapter;
	}

	@Bean(name = "receiveEmailChannel")
	public DirectChannel directChannel(MessageHandler messageHandler) {
		DirectChannel directChannel = new DirectChannel();
		directChannel.subscribe(messageHandler);
		return directChannel;
	}

	@Bean
	public MessageHandler messageHandler(ReceivingProperties properties, MailExtractionService mailExtractionService,
			OperationChain operationChain) {
		return new ReceivingMessageHandler(properties, mailExtractionService, operationChain);
	}
}
