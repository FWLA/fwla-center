package de.ihrigb.fwla.fwlacenter.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.ihrigb.fwla.fwlacenter.persistence.model.Resource;

public interface ResourceRepository extends JpaRepository<Resource, String> {

	Optional<Resource> findByKey(String key);
}
