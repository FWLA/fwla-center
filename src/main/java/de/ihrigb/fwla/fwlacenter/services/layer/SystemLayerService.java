package de.ihrigb.fwla.fwlacenter.services.layer;

import de.ihrigb.fwla.fwlacenter.persistence.repository.RealEstateRepository;
import de.ihrigb.fwla.fwlacenter.persistence.repository.StationRepository;

class SystemLayerService extends CompositeLayerService {

	SystemLayerService(StationRepository stationRepository, RealEstateRepository realEstateRepository) {
		super(new StationLayerAdapter(stationRepository), new RealEstateLayerAdapter(realEstateRepository));
	}
}
