package de.ihrigb.fwla.fwlacenter.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.ihrigb.fwla.fwlacenter.persistence.model.MapLayer;

public interface MapLayerRepository extends JpaRepository<MapLayer, String> {
}
