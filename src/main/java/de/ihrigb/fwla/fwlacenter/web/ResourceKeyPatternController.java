package de.ihrigb.fwla.fwlacenter.web;

import java.util.function.Function;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.ihrigb.fwla.fwlacenter.persistence.model.ResourceKeyPattern;
import de.ihrigb.fwla.fwlacenter.persistence.repository.ResourceKeyPatternRepository;
import de.ihrigb.fwla.fwlacenter.web.model.ResourceKeyPatternDTO;

@Transactional
@RestController
@RequestMapping("/v1/resourceKeyPatterns")
public class ResourceKeyPatternController extends BasePatternController<ResourceKeyPattern, ResourceKeyPatternDTO> {

	public ResourceKeyPatternController(ResourceKeyPatternRepository resourceKeyPatternRepository) {
		super(resourceKeyPatternRepository);
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

	@PostMapping
	public ResponseEntity<?> create(@RequestBody ResourceKeyPatternDTO dto) {
		return super.doCreate(dto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable("id") String id, @RequestBody ResourceKeyPatternDTO dto) {
		return super.doUpdate(id, dto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") String id) {
		return super.doDelete(id);
	}

	@Override
	protected Function<? super ResourceKeyPatternDTO, ? extends ResourceKeyPattern> getFromDTOFunction() {
		return dto -> dto.getPersistenceModel();
	}

	@Override
	protected Function<? super ResourceKeyPattern, ? extends ResourceKeyPatternDTO> getToDTOFunction() {
		return t -> new ResourceKeyPatternDTO(t);
	}
}
