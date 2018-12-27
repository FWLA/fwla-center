package de.ihrigb.fwla.fwlacenter.persistence.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import de.ihrigb.fwla.fwlacenter.persistence.model.AmbulancePattern;
import de.ihrigb.fwla.fwlacenter.persistence.model.PatternMode;

public interface AmbulancePatternRepository extends JpaRepository<AmbulancePattern, String> {
	Set<AmbulancePattern> findByMode(PatternMode mode);
}
