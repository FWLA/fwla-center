package de.ihrigb.fwla.fwlacenter.web;

import org.springframework.data.jpa.repository.JpaRepository;

import de.ihrigb.fwla.fwlacenter.persistence.model.BasePattern;
import de.ihrigb.fwla.fwlacenter.web.model.BasePatternDTO;

abstract class BasePatternController<T extends BasePattern, DTO extends BasePatternDTO> extends BaseController<T, String, DTO> {

	protected BasePatternController(JpaRepository<T, String> jpaRepository) {
		super(jpaRepository);
	}

	@Override
	protected String getId(T t) {
		return t.getId();
	}
}
