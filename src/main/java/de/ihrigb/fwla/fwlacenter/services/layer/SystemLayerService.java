package de.ihrigb.fwla.fwlacenter.services.layer;

import de.ihrigb.fwla.fwlacenter.persistence.repository.OperationRepository;
import de.ihrigb.fwla.fwlacenter.persistence.repository.RailwayCoordinateBoxRepository;
import de.ihrigb.fwla.fwlacenter.persistence.repository.RealEstateRepository;
import de.ihrigb.fwla.fwlacenter.persistence.repository.RiverSectorRepository;
import de.ihrigb.fwla.fwlacenter.persistence.repository.StationRepository;
import de.ihrigb.fwla.fwlacenter.services.api.OperationService;
import de.ihrigb.fwla.fwlacenter.services.river.CachingWSVRestServiceClient;

/**
 * @deprecated 0.1.4
 */
@Deprecated
class SystemLayerService extends CompositeLayerService {

	SystemLayerService(OperationRepository operationRepository, OperationService operationService,
			StationRepository stationRepository, RealEstateRepository realEstateRepository,
			RiverSectorRepository riverSectorRepository, CachingWSVRestServiceClient wsvRestClient,
			RailwayCoordinateBoxRepository railwayCoordinateBoxRepository) {
		super(new OperationLayerService(operationRepository, operationService),
				new StationLayerAdapter(stationRepository), new RealEstateLayerAdapter(realEstateRepository),
				new RiverLayerService(riverSectorRepository, wsvRestClient),
				new RailwayLayerService(railwayCoordinateBoxRepository));
	}
}
