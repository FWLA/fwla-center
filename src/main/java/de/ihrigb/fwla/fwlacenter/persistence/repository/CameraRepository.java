package de.ihrigb.fwla.fwlacenter.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.ihrigb.fwla.fwlacenter.persistence.model.Camera;

public interface CameraRepository extends JpaRepository<Camera, String> {
}
