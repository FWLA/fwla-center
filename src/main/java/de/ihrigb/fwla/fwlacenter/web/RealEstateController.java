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

import de.ihrigb.fwla.fwlacenter.persistence.model.RealEstate;
import de.ihrigb.fwla.fwlacenter.persistence.repository.RealEstateRepository;
import de.ihrigb.fwla.fwlacenter.persistence.repository.RealEstateTagRepository;
import de.ihrigb.fwla.fwlacenter.services.api.GeoServices;
import de.ihrigb.fwla.fwlacenter.utils.Sanitizers;
import de.ihrigb.fwla.fwlacenter.web.model.RealEstateDTO;

@Transactional
@RestController
@RequestMapping("/v1/realEstates")
public class RealEstateController extends BaseController<RealEstate, String, RealEstateDTO, RealEstateRepository> {

	private final RealEstateTagRepository realEstateTagRepository;
	private final GeoServices geoServices;

	public RealEstateController(RealEstateRepository repository, RealEstateTagRepository realEstateTagRepository, GeoServices geoServices) {
		super(repository);
		this.realEstateTagRepository = realEstateTagRepository;
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
	public ResponseEntity<?> create(@RequestBody RealEstateDTO dto) {
		return super.doCreate(dto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable("id") String id, @RequestBody RealEstateDTO dto) {
		return super.doUpdate(id, dto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") String id) {
		return super.doDelete(id);
	}

	@Override
	protected Function<? super RealEstate, ? extends RealEstateDTO> getToDTOFunction() {
		return realEstate -> {
			return new RealEstateDTO(realEstate);
		};
	}

	@Override
	protected Function<? super RealEstateDTO, ? extends RealEstate> getFromDTOFunction() {
		return dto -> {
			return dto.getPersistenceModel(realEstateTagRepository);
		};
	}

	@Override
	protected String getId(RealEstate t) {
		return t.getId();
	}

	@Override
	protected void beforeCreate(RealEstate entity) {
		if (entity.getLocation() != null) {
			if (entity.getLocation().getCoordinate() == null || entity.getLocation().getCoordinate().getLatitude() == 0d
					&& entity.getLocation().getCoordinate().getLongitude() == 0d) {
				setCoordinate(entity);
			}
		}
		Sanitizers.LOCATION_SANITIZER.accept(entity.getLocation());
	}

	@Override
	protected void beforeUpdate(RealEstate entity) {
		Sanitizers.LOCATION_SANITIZER.accept(entity.getLocation());
	}

	private void setCoordinate(RealEstate realEstate) {
		geoServices.geocoding().ifPresent(geocoding -> {
			geocoding.geocode(realEstate.getLocation());
		});
	}
}
