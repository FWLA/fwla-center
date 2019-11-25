package de.ihrigb.fwla.fwlacenter.persistence.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import de.ihrigb.fwla.fwlacenter.persistence.model.Roadblock;

public interface RoadblockRepository extends JpaRepository<Roadblock, String> {

	@Modifying
	@Query("DELETE Roadblock r WHERE r.endTime < CURRENT_TIMESTAMP()")
	void deleteOutdatedRoadblocks();

	@Query("select r from Roadblock r where "
		+ "(:swLatitude <= r.location.coordinate.latitude and :neLatitude >= r.location.coordinate.latitude) "
		+ "and "
		+ "(:swLongitude <= r.location.coordinate.longitude and :neLongitude >= r.location.coordinate.longitude)")
	Set<Roadblock> findWithinBounds(@Param("swLatitude") double swLatitude, @Param("swLongitude") double swLongitude,
			@Param("neLatitude") double neLatitude, @Param("neLongitude") double neLongitude);
}
