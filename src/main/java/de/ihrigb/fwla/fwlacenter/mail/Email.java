package de.ihrigb.fwla.fwlacenter.mail;

import java.time.Instant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Email {
	private final String sender;
	private final String subject;
	private final String body;
	private final Instant timestamp;
}
