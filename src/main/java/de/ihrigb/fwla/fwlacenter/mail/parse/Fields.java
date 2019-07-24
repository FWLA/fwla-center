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
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;

import de.ihrigb.fwla.fwlacenter.api.Address;
import de.ihrigb.fwla.fwlacenter.api.Coordinate;
import de.ihrigb.fwla.fwlacenter.api.Location;
import de.ihrigb.fwla.fwlacenter.persistence.model.Operation;
import de.ihrigb.fwla.fwlacenter.services.api.EventLogService;
import de.ihrigb.fwla.fwlacenter.utils.TriConsumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum Fields implements Field {

	ID {
		@Override
		public TriConsumer<Operation, Matcher, EventLogService> getPopulator() {
			return (operation, matcher, eventLogService) -> {
				operation.setId(getSingleValue(matcher).orElseGet(() -> {
					logBrokenRegex(this, eventLogService);
					return null;
				}));
			};
		}
	},
	TIME {
		@Override
		public TriConsumer<Operation, Matcher, EventLogService> getPopulator() {
			return (operation, matcher, eventLogService) -> {

				Instant time;
				String value = getSingleValue(matcher).orElseGet(() -> {
					logBrokenRegex(this, eventLogService);
					return null;
				});
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
		public TriConsumer<Operation, Matcher, EventLogService> getPopulator() {
			return (operation, matcher, eventLogService) -> {
				operation.setPlace(getSingleValue(matcher).orElseGet(() -> {
					logBrokenRegex(this, eventLogService);
					return null;
				}));
			};
		}
	},
	OBJECT {
		@Override
		public TriConsumer<Operation, Matcher, EventLogService> getPopulator() {
			return (operation, matcher, eventLogService) -> {
				operation.setObject(getSingleValue(matcher).orElseGet(() -> {
					logBrokenRegex(this, eventLogService);
					return null;
				}));
			};
		}
	},
	TOWN {
		@Override
		public TriConsumer<Operation, Matcher, EventLogService> getPopulator() {
			return (operation, matcher, eventLogService) -> {
				Fields.initLocation(operation);
				operation.getLocation().getAddress().setTown(getSingleValue(matcher).orElseGet(() -> {
					logBrokenRegex(this, eventLogService);
					return null;
				}));
			};
		}
	},
	DISTRICT {
		@Override
		public TriConsumer<Operation, Matcher, EventLogService> getPopulator() {
			return (operation, matcher, eventLogService) -> {
				Fields.initLocation(operation);
				operation.getLocation().getAddress().setDistrict(getSingleValue(matcher).orElseGet(() -> {
					logBrokenRegex(this, eventLogService);
					return null;
				}));
			};
		}
	},
	STREET {
		@Override
		public TriConsumer<Operation, Matcher, EventLogService> getPopulator() {
			return (operation, matcher, eventLogService) -> {
				Fields.initLocation(operation);
				operation.getLocation().getAddress().setStreet(getSingleValue(matcher).orElseGet(() -> {
					logBrokenRegex(this, eventLogService);
					return null;
				}));
			};
		}
	},
	LATITUDE {
		@Override
		public TriConsumer<Operation, Matcher, EventLogService> getPopulator() {
			return (operation, matcher, eventLogService) -> {
				if (operation.getLocation() == null) {
					operation.setLocation(new Location());
				}
				if (operation.getLocation().getCoordinate() == null) {
					operation.getLocation().setCoordinate(new Coordinate());
				}
				try {
					operation.getLocation().getCoordinate()
							.setLatitude(toDouble(getSingleValue(matcher).orElseGet(() -> {
								logBrokenRegex(this, eventLogService);
								return null;
							})));
				} catch (NumberFormatException e) {
					log.warn("Error parsing coordinate.", e);
				}
			};
		}
	},
	LONGITUDE {
		@Override
		public TriConsumer<Operation, Matcher, EventLogService> getPopulator() {
			return (operation, matcher, eventLogService) -> {
				if (operation.getLocation() == null) {
					operation.setLocation(new Location());
				}
				if (operation.getLocation().getCoordinate() == null) {
					operation.getLocation().setCoordinate(new Coordinate());
				}
				try {
					operation.getLocation().getCoordinate()
							.setLongitude(toDouble(getSingleValue(matcher).orElseGet(() -> {
								logBrokenRegex(this, eventLogService);
								return null;
							})));
				} catch (NumberFormatException e) {
					log.warn("Error parsing coordinate.", e);
				}
			};
		}
	},
	NOTICE {
		@Override
		public TriConsumer<Operation, Matcher, EventLogService> getPopulator() {
			return (operation, matcher, eventLogService) -> {
				operation.setNotice(getSingleValue(matcher).orElseGet(() -> {
					logBrokenRegex(this, eventLogService);
					return null;
				}));
			};
		}
	},
	MESSAGE {
		@Override
		public TriConsumer<Operation, Matcher, EventLogService> getPopulator() {
			return (operation, matcher, eventLogService) -> {
				operation.setMessage(getSingleValue(matcher).orElseGet(() -> {
					logBrokenRegex(this, eventLogService);
					return null;
				}));
			};
		}
	},
	CODE {
		@Override
		public TriConsumer<Operation, Matcher, EventLogService> getPopulator() {
			return (operation, matcher, eventLogService) -> {
				operation.setCode(getSingleValue(matcher).orElseGet(() -> {
					logBrokenRegex(this, eventLogService);
					return null;
				}));
			};
		}
	},
	RESOURCE_KEYS {
		@Override
		public TriConsumer<Operation, Matcher, EventLogService> getPopulator() {
			return (operation, matcher, eventLogService) -> {
				List<String> values = new ArrayList<>(getMultiValue(matcher));
				Collections.sort(values, String.CASE_INSENSITIVE_ORDER);
				operation.setResourceKeys(values);
			};
		}
	};

	static Optional<String> getSingleValue(Matcher matcher) {
		if (matcher.find()) {
			return Optional.of(trim(matcher.group(1)));
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

	private static void logBrokenRegex(Field field, EventLogService eventLogService) {
		eventLogService.error("Cannot find match for field %s", field.getName());
	}

	@Override
	public String getName() {
		return name();
	}
}
