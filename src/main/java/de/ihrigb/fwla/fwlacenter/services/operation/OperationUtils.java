package de.ihrigb.fwla.fwlacenter.services.operation;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.ihrigb.fwla.fwlacenter.persistence.model.Operation;
import de.ihrigb.fwla.fwlacenter.persistence.model.Resource;
import de.ihrigb.fwla.fwlacenter.persistence.model.Station;

public final class OperationUtils {

	public static Set<Station> getStations(Operation operation) {
		List<Resource> resources = operation.getResources();
		if (resources == null) {
			return Collections.emptySet();
		}

		return resources.stream().map(resource -> {
			return resource.getStation();
		}).collect(Collectors.toSet());
	}

	private OperationUtils() {
	}
}
