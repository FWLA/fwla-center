package de.ihrigb.fwla.fwlacenter.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.ihrigb.fwla.fwlacenter.persistence.model.RegexPattern;

public interface RegexPatternRepository extends JpaRepository<RegexPattern, String> {
}
