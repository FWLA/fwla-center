package de.ihrigb.fwla.fwlacenter.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.ihrigb.fwla.fwlacenter.persistence.model.RealEstateTag;

public interface RealEstateTagRepository extends JpaRepository<RealEstateTag, String> {
}
