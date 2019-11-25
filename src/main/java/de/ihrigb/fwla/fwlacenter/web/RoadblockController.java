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

import de.ihrigb.fwla.fwlacenter.persistence.model.Roadblock;
import de.ihrigb.fwla.fwlacenter.persistence.repository.RoadblockRepository;
import de.ihrigb.fwla.fwlacenter.services.api.GeoServices;
import de.ihrigb.fwla.fwlacenter.web.model.RoadblockDTO;

@Transactional
@RestController
@RequestMapping("/v1/roadblocks")
public class RoadblockController extends BaseController<Roadblock, String, RoadblockDTO, RoadblockRepository> {

	private final GeoServices geoServices;

	public RoadblockController(RoadblockRepository repository, GeoServices geoServices) {
		super(repository);
		this.geoServices = geoServices;
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
	public ResponseEntity<?> create(@RequestBody RoadblockDTO dto) {
		return super.doCreate(dto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable("id") String id, @RequestBody RoadblockDTO dto) {
		return super.doUpdate(id, dto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") String id) {
		return super.doDelete(id);
	}

	@Override
	protected Function<? super Roadblock, ? extends RoadblockDTO> getToDTOFunction() {
		return roadblock -> {
			return new RoadblockDTO(roadblock);
		};
	}

	@Override
	protected Function<? super RoadblockDTO, ? extends Roadblock> getFromDTOFunction() {
		return dto -> {
			return dto.getPersistenceModel();
		};
	}

	@Override
	protected String getId(Roadblock t) {
		return t.getId();
	}

	@Override
	protected void beforeCreate(Roadblock entity) {
		if (entity.getLocation().getCoordinate() == null || entity.getLocation().getCoordinate().getLatitude() == 0d
				&& entity.getLocation().getCoordinate().getLongitude() == 0d) {
			setCoordinate(entity);
		}
	}

	private void setCoordinate(Roadblock roadblock) {
		geoServices.geocoding().ifPresent(geocoding -> {
			geocoding.geocode(roadblock.getLocation());
		});
	}
}
