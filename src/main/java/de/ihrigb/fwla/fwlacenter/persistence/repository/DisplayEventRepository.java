package de.ihrigb.fwla.fwlacenter.persistence.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import de.ihrigb.fwla.fwlacenter.persistence.model.DisplayEvent;
import de.ihrigb.fwla.fwlacenter.persistence.model.Station;

public interface DisplayEventRepository extends JpaRepository<DisplayEvent, String> {

	@Modifying
	@Query("DELETE DisplayEvent e WHERE e.endTime < CURRENT_TIMESTAMP()")
	void deleteOutdatedDisplayEvents();

	@Query("SELECT e FROM DisplayEvent e WHERE e.station = :station and (e.startTime <= :endTime and :startTime <= e.endTime)")
	List<DisplayEvent> getEventsIntersectingTimeRange(@Param("station") Station station,
			@Param("startTime") Instant startTime, @Param("endTime") Instant endTime);

	@Query("SELECT e FROM DisplayEvent e WHERE e.station = :station and (e.startTime <= :endTime and :startTime <= e.endTime and e.id <> :excludeId)")
	List<DisplayEvent> getEventsIntersectingTimeRangeExcludeId(@Param("station") Station station,
			@Param("startTime") Instant startTime, @Param("endTime") Instant endTime,
			@Param("excludeId") String excludeId);

	@Query("SELECT e FROM DisplayEvent e WHERE e.station = :station and (e.startTime <= CURRENT_TIMESTAMP() and e.endTime > CURRENT_TIMESTAMP())")
	Optional<DisplayEvent> getActive(@Param("station") Station station);
}
