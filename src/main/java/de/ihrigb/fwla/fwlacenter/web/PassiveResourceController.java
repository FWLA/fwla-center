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

import de.ihrigb.fwla.fwlacenter.persistence.model.PassiveResource;
import de.ihrigb.fwla.fwlacenter.persistence.repository.ActiveResourceRepository;
import de.ihrigb.fwla.fwlacenter.persistence.repository.PassiveResourceRepository;
import de.ihrigb.fwla.fwlacenter.persistence.repository.StationRepository;
import de.ihrigb.fwla.fwlacenter.web.model.PassiveResourceDTO;

@RestController
@RequestMapping("/v1/passiveResources")
public class PassiveResourceController extends BaseController<PassiveResource, String, PassiveResourceDTO> {

	private final StationRepository stationRepository;
	private final ActiveResourceRepository activeResourceRepository;

	public PassiveResourceController(PassiveResourceRepository repository,
			ActiveResourceRepository activeResourceRepository, StationRepository stationRepository) {
		super(repository);
		this.stationRepository = stationRepository;
		this.activeResourceRepository = activeResourceRepository;
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
	public ResponseEntity<?> create(@RequestBody PassiveResourceDTO dto) {
		return super.doCreate(dto);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable("id") String id, @RequestBody PassiveResourceDTO dto) {
		return super.doUpdate(id, dto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") String id) {
		return super.doDelete(id);
	}

	@Override
	protected Function<? super PassiveResource, ? extends PassiveResourceDTO> getToDTOFunction() {
		return passiveResource -> {
			return new PassiveResourceDTO(passiveResource);
		};
	}

	@Override
	protected Function<? super PassiveResourceDTO, ? extends PassiveResource> getFromDTOFunction() {
		return dto -> {
			return dto.getPersistenceModel(stationRepository, activeResourceRepository);
		};
	}

	@Override
	protected String getId(PassiveResource t) {
		return t.getId();
	}
}
