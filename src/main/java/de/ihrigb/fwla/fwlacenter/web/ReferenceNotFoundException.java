package de.ihrigb.fwla.fwlacenter.web;

public class ReferenceNotFoundException extends RuntimeException {

	private final static long serialVersionUID = 1L;

	public ReferenceNotFoundException(String message) {
		super(message);
	}
}
