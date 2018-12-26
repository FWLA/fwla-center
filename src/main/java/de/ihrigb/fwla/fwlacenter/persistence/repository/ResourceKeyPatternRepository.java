package de.ihrigb.fwla.fwlacenter.persistence.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import de.ihrigb.fwla.fwlacenter.persistence.model.PatternMode;
import de.ihrigb.fwla.fwlacenter.persistence.model.ResourceKeyPattern;

public interface ResourceKeyPatternRepository extends JpaRepository<ResourceKeyPattern, String> {
	Set<ResourceKeyPattern> findByMode(PatternMode mode);
}
