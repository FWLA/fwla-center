package de.ihrigb.fwla.fwlacenter.utils;

import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.util.ReflectionUtils;

import de.ihrigb.fwla.fwlacenter.api.Address;
import de.ihrigb.fwla.fwlacenter.api.Location;

public final class Sanitizers {

	public static void clearWhitespaceOnlyFields(Object o) {
		Sanitizers.clearWhitespaceOnlyFields(o, new String[0]);
	}

	public static void clearWhitespaceOnlyFields(Object o, String... excludedFieldNames) {
		ReflectionUtils.doWithFields(o.getClass(), field -> {
			// can only be string due to filter
			String value = (String) field.get(o);
			if (value == null) {
				return;
			}

			if ("".equals(value.trim())) {
				field.set(o, null);
			}

		}, field -> {
			if (field.getType() != String.class) {
				return false;
			}

			if (excludedFieldNames == null || excludedFieldNames.length == 0) {
				return true;
			}

			return ArrayUtils.contains(excludedFieldNames, field.getName());
		});
	}

	public static final Function<String, String> STRING_SANITIZER = (string) -> {
		if (string == null) {
			return null;
		}

		while (string.contains("  ")) {
			string = string.replace("  ", " ");
		}

		return string.trim();
	};

	public static final Function<String, String> STREET_SANITIZER = STRING_SANITIZER.andThen((street) -> {
		if (street == null) {
			return null;
		}

		return street.replaceAll("Str\\.", "Straße").replaceAll("str\\.", "straße").replaceAll("Strasse", "Straße")
				.replaceAll("strasse", "straße");
	});

	public static final Consumer<Address> ADDRESS_SANITIZER = (address) -> {
		address.setStreet(STREET_SANITIZER.apply(address.getStreet()));
	};

	public static final Consumer<Location> LOCATION_SANITIZER = (location) -> {
		ADDRESS_SANITIZER.accept(location.getAddress());
	};

	private Sanitizers() {
	}
}
