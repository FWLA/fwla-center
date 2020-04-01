package de.ihrigb.fwla.fwlacenter.mail.receive;

import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.mail.receive")
public class ReceivingProperties {

	private String protocol;
	private String host;
	private int port;
	private String username;
	private String password;
	private Set<String> whitelistHot;
	private Set<String> whitelistTraining;
	private boolean debug = false;
}
