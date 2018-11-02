package de.ihrigb.fwla.fwlacenter.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.ihrigb.fwla.fwlacenter.persistence.model.OperationKey;

public interface OperationKeyRepository extends JpaRepository<OperationKey, String> {}
