package de.ihrigb.fwla.fwlacenter.persistence.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import de.ihrigb.fwla.fwlacenter.persistence.model.River;
import de.ihrigb.fwla.fwlacenter.persistence.model.RiverSector;

public interface RiverSectorRepository extends JpaRepository<RiverSector, String> {

	@Query("select r.river from RiverSector r group by r.river")
	Set<River> findRivers();

	Set<RiverSector> findByRiver(River river);
}
