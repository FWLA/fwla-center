package de.ihrigb.fwla.fwlacenter.web;

import java.util.function.Function;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.ihrigb.fwla.fwlacenter.persistence.model.OperationKey;
import de.ihrigb.fwla.fwlacenter.persistence.repository.OperationKeyRepository;
import de.ihrigb.fwla.fwlacenter.web.model.OperationKeyDTO;

@RestController
@RequestMapping("/v1/operationKeys")
public class OperationKeyController extends BaseController<OperationKey, String, OperationKeyDTO> {

	public OperationKeyController(OperationKeyRepository repository) {
		super(repository);
	}

	@GetMapping
	public ResponseEntity<?> getAll(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "filter", required = false) String filter) {
		return super.doGetAll(page, size, filter);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getOne(@PathVariable("id") String id) {
		return super.doGetOne(id);
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody OperationKeyDTO dto) {
		return super.doCreate(dto);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable("id") String id, @RequestBody OperationKeyDTO dto) {
		return super.doUpdate(id, dto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") String id) {
		return super.doDelete(id);
	}

	@Override
	protected Function<? super OperationKey, ? extends OperationKeyDTO> getToDTOFunction() {
		return operationKey -> {
			return new OperationKeyDTO(operationKey);
		};
	}

	@Override
	protected Function<? super OperationKeyDTO, ? extends OperationKey> getFromDTOFunction() {
		return dto -> {
			return dto.getPersistenceModel();
		};
	}

	@Override
	protected String getId(OperationKey t) {
		return t.getId();
	}
}