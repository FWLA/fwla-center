package de.ihrigb.fwla.fwlacenter.services.roadblock;

import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.ihrigb.fwla.fwlacenter.api.Coordinate;
import de.ihrigb.fwla.fwlacenter.persistence.model.Roadblock;
import de.ihrigb.fwla.fwlacenter.persistence.repository.RoadblockRepository;
import de.ihrigb.fwla.fwlacenter.services.api.RoadblockService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoadblockServiceImpl implements RoadblockService {

	private final RoadblockRepository roadblockRepository;

	@Transactional
	@Scheduled(fixedRate = 60000)
	public void deleteOutdatedRoadblocks() {
		roadblockRepository.deleteOutdatedRoadblocks();
	}

	@Override
	public Set<Roadblock> getWithinBounds(Coordinate upperLeftCoordinate, Coordinate lowerRightCoordinate) {
		return roadblockRepository.findWithinBounds(upperLeftCoordinate.getLatitude(),
				upperLeftCoordinate.getLongitude(), lowerRightCoordinate.getLatitude(),
				lowerRightCoordinate.getLongitude());
	}
}
