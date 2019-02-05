package de.ihrigb.fwla.fwlacenter.web;

import java.util.function.Function;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.ihrigb.fwla.fwlacenter.persistence.model.RegexPattern;
import de.ihrigb.fwla.fwlacenter.persistence.repository.RegexPatternRepository;
import de.ihrigb.fwla.fwlacenter.web.model.RegexPatternDTO;

@RestController
@RequestMapping("/v1/regexPatterns")
public class RegexPatternController extends BaseController<RegexPattern, String, RegexPatternDTO> {

	public RegexPatternController(RegexPatternRepository repository) {
		super(repository);
	}

	@GetMapping
	public ResponseEntity<?> getAll(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "filter", required = false) String filter,
			@RequestParam(name = "sort", required = false) String sort) {
		return super.doGetAll(page, size, filter, sort);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getOne(@PathVariable("id") String id) {
		return super.doGetOne(id);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable("id") String id, @RequestBody RegexPatternDTO dto) {
		return super.doUpdate(id, dto);
	}

	@Override
	protected Function<? super RegexPattern, ? extends RegexPatternDTO> getToDTOFunction() {
		return regexPattern -> {
			return new RegexPatternDTO(regexPattern);
		};
	}

	@Override
	protected Function<? super RegexPatternDTO, ? extends RegexPattern> getFromDTOFunction() {
		return dto -> {
			return dto.getPersistenceModel();
		};
	}

	@Override
	protected String getId(RegexPattern t) {
		return t.getId();
	}
}
