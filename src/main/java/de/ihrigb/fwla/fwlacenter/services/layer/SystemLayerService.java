package de.ihrigb.fwla.fwlacenter.services.layer;

import de.ihrigb.fwla.fwlacenter.persistence.repository.OperationRepository;
import de.ihrigb.fwla.fwlacenter.persistence.repository.RealEstateRepository;
import de.ihrigb.fwla.fwlacenter.persistence.repository.StationRepository;
import de.ihrigb.fwla.fwlacenter.services.api.OperationService;

class SystemLayerService extends CompositeLayerService {

	SystemLayerService(OperationRepository operationRepository, OperationService operationService,
			StationRepository stationRepository, RealEstateRepository realEstateRepository) {
		super(new OperationLayerService(operationRepository, operationService),
				new StationLayerAdapter(stationRepository), new RealEstateLayerAdapter(realEstateRepository));
	}
}
