package de.ihrigb.fwla.fwlacenter.web;

import java.util.function.Function;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.ihrigb.fwla.fwlacenter.persistence.model.Hydrant;
import de.ihrigb.fwla.fwlacenter.persistence.repository.HydrantRepository;
import de.ihrigb.fwla.fwlacenter.services.api.GeoServices;
import de.ihrigb.fwla.fwlacenter.web.model.HydrantDTO;

@RestController
@RequestMapping("/v1/hydrants")
public class HydrantController extends BaseController<Hydrant, String, HydrantDTO> {

	private final GeoServices geoServices;

	public HydrantController(HydrantRepository repository, GeoServices geoServices) {
		super(repository);
		this.geoServices = geoServices;
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
	public ResponseEntity<?> create(@RequestBody HydrantDTO dto) {
		return super.doCreate(dto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable("id") String id, @RequestBody HydrantDTO dto) {
		return super.doUpdate(id, dto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") String id) {
		return super.doDelete(id);
	}

	@Override
	protected Function<? super Hydrant, ? extends HydrantDTO> getToDTOFunction() {
		return hydrant -> {
			return new HydrantDTO(hydrant);
		};
	}

	@Override
	protected Function<? super HydrantDTO, ? extends Hydrant> getFromDTOFunction() {
		return dto -> {
			return dto.getPersistenceModel();
		};
	}

	@Override
	protected String getId(Hydrant t) {
		return t.getId();
	}

	@Override
	protected void beforeCreate(Hydrant entity) {
		if (entity.getLocation().getCoordinate() == null || entity.getLocation().getCoordinate().getLatitude() == 0d
				&& entity.getLocation().getCoordinate().getLongitude() == 0d) {
			setCoordinate(entity);
		}
	}

	private void setCoordinate(Hydrant realEstate) {
		geoServices.geocoding().ifPresent(geocoding -> {
			geocoding.geocode(realEstate.getLocation());
		});
	}
}
