package de.ihrigb.fwla.fwlacenter.services.layer;

import org.springframework.stereotype.Component;

import de.ihrigb.fwla.fwlacenter.persistence.repository.RealEstateRepository;
import de.ihrigb.fwla.fwlacenter.persistence.repository.StationRepository;
import de.ihrigb.fwla.fwlacenter.services.api.OperationService;

@Component
public class MasterLayerService extends CompositeLayerService {

	public MasterLayerService(OperationService operationService, StationRepository stationRepository,
			RealEstateRepository realEstateRepository) {
		super(new SystemLayerService(operationService, stationRepository, realEstateRepository));
	}
}
