package de.ihrigb.fwla.fwlacenter.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.ihrigb.fwla.fwlacenter.persistence.model.RealEstate;

public interface RealEstateRepository extends JpaRepository<RealEstate, String> {

	Optional<RealEstate> findOneByKey(String key);
}
