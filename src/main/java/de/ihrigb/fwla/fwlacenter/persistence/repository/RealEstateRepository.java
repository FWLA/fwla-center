package de.ihrigb.fwla.fwlacenter.persistence.repository;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import de.ihrigb.fwla.fwlacenter.persistence.model.RealEstate;

public interface RealEstateRepository extends JpaRepository<RealEstate, String> {

	Optional<RealEstate> findOneByName(String key);

	@Query("select r from RealEstate r")
	Stream<RealEstate> streamAll();
}
