package de.ihrigb.fwla.fwlacenter.services.layer;

import de.ihrigb.fwla.fwlacenter.persistence.repository.RealEstateRepository;
import de.ihrigb.fwla.fwlacenter.persistence.repository.StationRepository;
import de.ihrigb.fwla.fwlacenter.services.api.OperationService;

class SystemLayerService extends CompositeLayerService {

	SystemLayerService(OperationService operationService, StationRepository stationRepository,
			RealEstateRepository realEstateRepository) {
		super(new OperationLayerService(operationService), new StationLayerAdapter(stationRepository),
				new RealEstateLayerAdapter(realEstateRepository));
	}
}
