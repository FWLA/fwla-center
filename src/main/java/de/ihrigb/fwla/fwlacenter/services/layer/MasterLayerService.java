package de.ihrigb.fwla.fwlacenter.services.layer;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import de.ihrigb.fwla.fwlacenter.persistence.repository.OperationRepository;
import de.ihrigb.fwla.fwlacenter.persistence.repository.RailwayCoordinateBoxRepository;
import de.ihrigb.fwla.fwlacenter.persistence.repository.RealEstateRepository;
import de.ihrigb.fwla.fwlacenter.persistence.repository.RiverSectorRepository;
import de.ihrigb.fwla.fwlacenter.persistence.repository.StationRepository;
import de.ihrigb.fwla.fwlacenter.services.api.OperationService;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerGroup;
import de.ihrigb.fwla.fwlacenter.services.river.CachingWSVRestServiceClient;

/**
 * @deprecated 0.1.4
 */
@Deprecated
@Component
public class MasterLayerService extends CompositeLayerService {

	public MasterLayerService(OperationRepository operationRepository, OperationService operationService,
			StationRepository stationRepository, RealEstateRepository realEstateRepository,
			RiverSectorRepository riverSectorRepository, CachingWSVRestServiceClient wsvRestClient,
			RailwayCoordinateBoxRepository railwayCoordinateBoxRepository) {
		super(new SystemLayerService(operationRepository, operationService, stationRepository, realEstateRepository,
				riverSectorRepository, wsvRestClient, railwayCoordinateBoxRepository));
	}

	@Override
	public List<LayerGroup> getLayerGroups() {
		List<LayerGroup> result = super.getLayerGroups();

		result.forEach(layerGroup -> {
			layerGroup.sortLayers();
		});

		Collections.sort(result);

		return result;
	}
}
