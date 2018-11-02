package de.ihrigb.fwla.fwlacenter.mail.parse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.function.BiConsumer;

import de.ihrigb.fwla.fwlacenter.services.api.Coordinate;
import de.ihrigb.fwla.fwlacenter.services.api.Location;
import de.ihrigb.fwla.fwlacenter.services.api.Operation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum Fields implements Field {

	ID {
		@Override
		public BiConsumer<Operation, String> getPopulator() {
			return (operation, value) -> {
				operation.setId(value);
			};
		}
	},
	TIME {
		@Override
		public BiConsumer<Operation, String> getPopulator() {
			return (operation, value) -> {
				SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

				Instant time;
				try {
					Date date = format.parse(value.trim());
					time = date.toInstant();
				} catch (ParseException e) {
					log.error("Error parsing time. Fallback to now.", e);
					time = Instant.now();
				}

				operation.setTime(time);
			};
		}
	},
	PLACE {
		@Override
		public BiConsumer<Operation, String> getPopulator() {
			return (operation, value) -> {
				operation.setPlace(value);
			};
		}
	},
	OBJECT {
		@Override
		public BiConsumer<Operation, String> getPopulator() {
			return (operation, value) -> {
				operation.setObject(value);
			};
		}
	},
	TOWN {
		@Override
		public BiConsumer<Operation, String> getPopulator() {
			return (operation, value) -> {
				if (operation.getLocation() == null) {
					operation.setLocation(new Location());
				}
				operation.getLocation().setTown(value);
			};
		}
	},
	DISTRICT {
		@Override
		public BiConsumer<Operation, String> getPopulator() {
			return (operation, value) -> {
				if (operation.getLocation() == null) {
					operation.setLocation(new Location());
				}
				operation.getLocation().setDistrict(value);
			};
		}
	},
	STREET {
		@Override
		public BiConsumer<Operation, String> getPopulator() {
			return (operation, value) -> {
				if (operation.getLocation() == null) {
					operation.setLocation(new Location());
				}
				operation.getLocation().setStreet(value);
			};
		}
	},
	LATITUDE {
		@Override
		public BiConsumer<Operation, String> getPopulator() {
			return (operation, value) -> {
				if (operation.getLocation() == null) {
					operation.setLocation(new Location());
				}
				if (operation.getLocation().getCoordinate() == null) {
					operation.getLocation().setCoordinate(new Coordinate());
				}
				try {
					operation.getLocation().getCoordinate().setLatitude(Double.parseDouble(value));
				} catch (NumberFormatException e) {
					log.warn("Error parsing coordinate.", e);
				}
			};
		}
	},
	LONGITUDE {
		@Override
		public BiConsumer<Operation, String> getPopulator() {
			return (operation, value) -> {
				if (operation.getLocation() == null) {
					operation.setLocation(new Location());
				}
				if (operation.getLocation().getCoordinate() == null) {
					operation.getLocation().setCoordinate(new Coordinate());
				}
				try {
					operation.getLocation().getCoordinate().setLongitude(Double.parseDouble(value));
				} catch (NumberFormatException e) {
					log.warn("Error parsing coordinate.", e);
				}
			};
		}
	},
	NOTICE {
		@Override
		public BiConsumer<Operation, String> getPopulator() {
			return (operation, value) -> {
				operation.setNotice(value);
			};
		}
	},
	MESSAGE {
		@Override
		public BiConsumer<Operation, String> getPopulator() {
			return (operation, value) -> {
				operation.setMessage(value);
			};
		}
	},
	CODE {
		@Override
		public BiConsumer<Operation, String> getPopulator() {
			return (operation, value) -> {
				operation.setCode(value);
			};
		}
	};

	@Override
	public String getName() {
		return name();
	}
}
