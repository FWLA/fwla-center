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

import de.ihrigb.fwla.fwlacenter.persistence.model.ActiveResource;
import de.ihrigb.fwla.fwlacenter.persistence.repository.ActiveResourceRepository;
import de.ihrigb.fwla.fwlacenter.persistence.repository.StationRepository;
import de.ihrigb.fwla.fwlacenter.web.model.ActiveResourceDTO;

@RestController
@RequestMapping("/v1/activeResources")
public class ActiveResourceController extends BaseController<ActiveResource, String, ActiveResourceDTO> {

	private final StationRepository stationRepository;

	public ActiveResourceController(ActiveResourceRepository repository, StationRepository stationRepository) {
		super(repository);
		this.stationRepository = stationRepository;
	}

	@GetMapping
	public ResponseEntity<?> getAll(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "filter", required = false) String filter) {
		return super.doGetAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getOne(@PathVariable("id") String id) {
		return super.doGetOne(id);
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody ActiveResourceDTO dto) {
		return super.doCreate(dto);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable("id") String id, @RequestBody ActiveResourceDTO dto) {
		return super.doUpdate(id, dto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") String id) {
		return super.doDelete(id);
	}

	@Override
	protected Function<? super ActiveResource, ? extends ActiveResourceDTO> getToDTOFunction() {
		return activeResource -> {
			return new ActiveResourceDTO(activeResource);
		};
	}

	@Override
	protected Function<? super ActiveResourceDTO, ? extends ActiveResource> getFromDTOFunction() {
		return dto -> {
			return dto.getPersistenceModel(stationRepository);
		};
	}

	@Override
	protected String getId(ActiveResource t) {
		return t.getId();
	}
}
