package de.ihrigb.fwla.fwlacenter.services.layer;

import org.springframework.stereotype.Component;

import de.ihrigb.fwla.fwlacenter.persistence.repository.RealEstateRepository;
import de.ihrigb.fwla.fwlacenter.persistence.repository.StationRepository;

@Component
public class MasterLayerService extends CompositeLayerService {

	public MasterLayerService(StationRepository stationRepository, RealEstateRepository realEstateRepository) {
		super(new SystemLayerService(stationRepository, realEstateRepository));
	}
}
