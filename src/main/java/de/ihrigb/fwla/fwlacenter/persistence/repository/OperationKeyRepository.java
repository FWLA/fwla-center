package de.ihrigb.fwla.fwlacenter.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.ihrigb.fwla.fwlacenter.persistence.model.OperationKey;

public interface OperationKeyRepository extends JpaRepository<OperationKey, String> {
	Optional<OperationKey> findOneByCode(String code);
}
