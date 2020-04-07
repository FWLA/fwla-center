package de.ihrigb.fwla.fwlacenter.mail.receive;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Properties;

import javax.net.ssl.SSLSocketFactory;

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
import de.ihrigb.fwla.mail.EmailHandler;
import de.ihrigb.fwla.mail.EmailSenderFilter;
import de.ihrigb.fwla.mail.ReceivingMessageHandler;
import de.ihrigb.fwla.mail.TextEmailBodyConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableIntegration
@ConditionalOnProperty("app.mail.receive.host")
@EnableConfigurationProperties(ReceivingProperties.class)
public class MailConfiguration {

	@Bean
	public ImapMailReceiver imapMailReceiver(ReceivingProperties properties) {

		Properties javaMailProperties = new Properties();
		javaMailProperties.setProperty("mail.imaps.socketFactory.class", SSLSocketFactory.class.getName());
		javaMailProperties.setProperty("mail.imap.starttls.enable", "true");
		javaMailProperties.setProperty("mail.imaps.socketFactory.fallback", "false");
		javaMailProperties.setProperty("mail.store.protocol", properties.getProtocol());
		javaMailProperties.setProperty("mail.debug", "" + properties.isDebug());

		String userInfo = String.format("%s:%s", properties.getUsername(), properties.getPassword());

		String url = String.format("%s://%s@%s:%d/INBOX", properties.getProtocol(),
				UriUtils.encodeUserInfo(userInfo, StandardCharsets.UTF_8),
				UriUtils.encodeHost(properties.getHost(), StandardCharsets.UTF_8), properties.getPort());

		log.info("Connecting to {}.", url);
		ImapMailReceiver imapMailReceiver = new ImapMailReceiver(url);
		imapMailReceiver.setShouldMarkMessagesAsRead(Boolean.TRUE);
		imapMailReceiver.setJavaMailProperties(javaMailProperties);
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
		EmailSenderFilter emailSenderFilter = new MailFilter(properties);
		EmailHandler<String> emailHandler = new OperationEmailHandler(properties, mailExtractionService,
				operationChain);
		return new ReceivingMessageHandler<>(Optional.of(emailSenderFilter), new TextEmailBodyConverter(),
				emailHandler);
	}
}
