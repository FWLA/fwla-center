package de.ihrigb.fwla.fwlacenter.web.exception;

import java.util.Optional;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {

	private final static long serialVersionUID = 1L;

	private final Optional<?> entity;

	public BadRequestException(String message) {
		this(message, Optional.empty());
	}
	
	public BadRequestException(String message, Optional<?> entity) {
		super(message);
		this.entity = entity;
	}
}
