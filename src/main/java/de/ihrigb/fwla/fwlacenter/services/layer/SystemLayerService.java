package de.ihrigb.fwla.fwlacenter.services.layer;

import de.ihrigb.fwla.fwlacenter.persistence.repository.OperationRepository;
import de.ihrigb.fwla.fwlacenter.persistence.repository.RealEstateRepository;
import de.ihrigb.fwla.fwlacenter.persistence.repository.StationRepository;

class SystemLayerService extends CompositeLayerService {

	SystemLayerService(OperationRepository operationRepository, StationRepository stationRepository,
			RealEstateRepository realEstateRepository) {
		super(new OperationLayerService(operationRepository), new StationLayerAdapter(stationRepository),
				new RealEstateLayerAdapter(realEstateRepository));
	}
}
