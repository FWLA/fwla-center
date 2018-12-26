package de.ihrigb.fwla.fwlacenter.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.ihrigb.fwla.fwlacenter.persistence.model.RegexPattern;
import de.ihrigb.fwla.fwlacenter.persistence.model.Source;

public interface RegexPatternRepository extends JpaRepository<RegexPattern, String> {
	Optional<RegexPattern> findBySource(Source source);
}
