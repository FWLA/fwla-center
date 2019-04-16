package de.ihrigb.fwla.fwlacenter.web;

import org.springframework.data.jpa.repository.JpaRepository;

import de.ihrigb.fwla.fwlacenter.persistence.model.BasePattern;
import de.ihrigb.fwla.fwlacenter.web.model.BasePatternDTO;

abstract class BasePatternController<T extends BasePattern, DTO extends BasePatternDTO, Repository extends JpaRepository<T, String>>
		extends BaseController<T, String, DTO, Repository> {

	protected BasePatternController(Repository jpaRepository) {
		super(jpaRepository);
	}

	@Override
	protected String getId(T t) {
		return t.getId();
	}
}
