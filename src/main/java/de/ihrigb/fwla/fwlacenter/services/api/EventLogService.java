package de.ihrigb.fwla.fwlacenter.services.api;

public interface EventLogService {

	void info(String message);

	default void info(String message, String... args) {
		info(String.format(message, (Object[]) args));
	}

	void error(String message);

	default void error(String message, String... args) {
		error(String.format(message, (Object[]) args));
	}

}
