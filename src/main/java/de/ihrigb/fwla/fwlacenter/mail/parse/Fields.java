package de.ihrigb.fwla.fwlacenter.mail.parse;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;

import de.ihrigb.fwla.fwlacenter.api.Address;
import de.ihrigb.fwla.fwlacenter.api.Coordinate;
import de.ihrigb.fwla.fwlacenter.api.Location;
import de.ihrigb.fwla.fwlacenter.services.api.Operation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum Fields implements Field {

	ID {
		@Override
		public BiConsumer<Operation, Matcher> getPopulator() {
			return (operation, matcher) -> {
				operation.setId(getSingleValue(matcher));
			};
		}
	},
	TIME {
		@Override
		public BiConsumer<Operation, Matcher> getPopulator() {
			return (operation, matcher) -> {

				Instant time;
				String value = getSingleValue(matcher);
				if (value != null) {
					try {
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
						LocalDateTime localDateTime = LocalDateTime.parse(value, formatter);

						ZoneId zoneId = ZoneId.of("Europe/Berlin");
						ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, zoneId);
						time = zonedDateTime.toInstant();
					} catch (RuntimeException e) {
						log.error("Error parsing time. Fallback to now.", e);
						time = Instant.now();
					}
				} else {
					time = Instant.now();
				}

				operation.setTime(time);
			};
		}
	},
	PLACE {
		@Override
		public BiConsumer<Operation, Matcher> getPopulator() {
			return (operation, matcher) -> {
				operation.setPlace(getSingleValue(matcher));
			};
		}
	},
	OBJECT {
		@Override
		public BiConsumer<Operation, Matcher> getPopulator() {
			return (operation, matcher) -> {
				operation.setObject(getSingleValue(matcher));
			};
		}
	},
	TOWN {
		@Override
		public BiConsumer<Operation, Matcher> getPopulator() {
			return (operation, matcher) -> {
				Fields.initLocation(operation);
				operation.getLocation().getAddress().setTown(getSingleValue(matcher));
			};
		}
	},
	DISTRICT {
		@Override
		public BiConsumer<Operation, Matcher> getPopulator() {
			return (operation, matcher) -> {
				Fields.initLocation(operation);
				operation.getLocation().getAddress().setDistrict(getSingleValue(matcher));
			};
		}
	},
	STREET {
		@Override
		public BiConsumer<Operation, Matcher> getPopulator() {
			return (operation, matcher) -> {
				Fields.initLocation(operation);
				operation.getLocation().getAddress().setStreet(getSingleValue(matcher));
			};
		}
	},
	LATITUDE {
		@Override
		public BiConsumer<Operation, Matcher> getPopulator() {
			return (operation, matcher) -> {
				if (operation.getLocation() == null) {
					operation.setLocation(new Location());
				}
				if (operation.getLocation().getCoordinate() == null) {
					operation.getLocation().setCoordinate(new Coordinate());
				}
				try {
					operation.getLocation().getCoordinate().setLatitude(toDouble(getSingleValue(matcher)));
				} catch (NumberFormatException e) {
					log.warn("Error parsing coordinate.", e);
				}
			};
		}
	},
	LONGITUDE {
		@Override
		public BiConsumer<Operation, Matcher> getPopulator() {
			return (operation, matcher) -> {
				if (operation.getLocation() == null) {
					operation.setLocation(new Location());
				}
				if (operation.getLocation().getCoordinate() == null) {
					operation.getLocation().setCoordinate(new Coordinate());
				}
				try {
					operation.getLocation().getCoordinate().setLongitude(toDouble(getSingleValue(matcher)));
				} catch (NumberFormatException e) {
					log.warn("Error parsing coordinate.", e);
				}
			};
		}
	},
	NOTICE {
		@Override
		public BiConsumer<Operation, Matcher> getPopulator() {
			return (operation, matcher) -> {
				operation.setNotice(getSingleValue(matcher));
			};
		}
	},
	MESSAGE {
		@Override
		public BiConsumer<Operation, Matcher> getPopulator() {
			return (operation, matcher) -> {
				operation.setMessage(getSingleValue(matcher));
			};
		}
	},
	CODE {
		@Override
		public BiConsumer<Operation, Matcher> getPopulator() {
			return (operation, matcher) -> {
				operation.setCode(getSingleValue(matcher));
			};
		}
	},
	RESOURCE_KEYS {
		@Override
		public BiConsumer<Operation, Matcher> getPopulator() {
			return (operation, matcher) -> {
				List<String> values = new ArrayList<>(getMultiValue(matcher));
				Collections.sort(values, String.CASE_INSENSITIVE_ORDER);
				operation.setResourceKeys(values);
			};
		}
	};

	static String getSingleValue(Matcher matcher) {
		if (matcher.find()) {
			return trim(matcher.group(1));
		}
		return null;
	}

	static Set<String> getMultiValue(Matcher matcher) {
		Set<String> values = new HashSet<>();
		while (matcher.find()) {
			values.add(trim(matcher.group(1)));
		}
		return values;
	}

	static String trim(String value) {
		if (value == null) {
			return null;
		}

		String trimmed = value.trim();
		if ("".equals(trimmed)) {
			return null;
		}

		return trimmed;
	}

	static double toDouble(String value) {
		if (value == null) {
			return 0;
		}
		return Double.parseDouble(value);
	}

	private static void initLocation(Operation operation) {
		if (operation.getLocation() == null) {
			operation.setLocation(new Location());
		}
		if (operation.getLocation().getAddress() == null) {
			operation.getLocation().setAddress(new Address());
		}
	}

	@Override
	public String getName() {
		return name();
	}
}
