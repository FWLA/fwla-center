package de.ihrigb.fwla.fwlacenter.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.ihrigb.fwla.fwlacenter.persistence.model.RailwayCoordinateBox;

public interface RailwayCoordinateBoxRepository extends JpaRepository<RailwayCoordinateBox, String> {
}
