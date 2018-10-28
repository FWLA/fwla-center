package de.ihrigb.fwla.fwlacenter.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.ihrigb.fwla.fwlacenter.persistence.model.Station;

public interface StationRepository extends JpaRepository<Station, String> {}
