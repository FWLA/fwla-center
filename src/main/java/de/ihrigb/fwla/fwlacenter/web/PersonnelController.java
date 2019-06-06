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

import de.ihrigb.fwla.fwlacenter.api.Location;
import de.ihrigb.fwla.fwlacenter.persistence.model.Personnel;
import de.ihrigb.fwla.fwlacenter.persistence.repository.PersonnelRepository;
import de.ihrigb.fwla.fwlacenter.persistence.repository.StationRepository;
import de.ihrigb.fwla.fwlacenter.services.api.GeoServices;
import de.ihrigb.fwla.fwlacenter.web.model.PersonnelDTO;

@Transactional
@RestController
@RequestMapping("/v1/personnel")
public class PersonnelController extends BaseController<Personnel, String, PersonnelDTO, PersonnelRepository> {

	private final StationRepository stationRepository;
	private final GeoServices geoServices;

	public PersonnelController(PersonnelRepository repository, StationRepository stationRepository, GeoServices geoServices) {
		super(repository);
		this.stationRepository = stationRepository;
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
	public ResponseEntity<?> create(@RequestBody PersonnelDTO dto) {
		return super.doCreate(dto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable("id") String id, @RequestBody PersonnelDTO dto) {
		return super.doUpdate(id, dto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") String id) {
		return super.doDelete(id);
	}

	@Override
	protected Function<? super Personnel, ? extends PersonnelDTO> getToDTOFunction() {
		return personnel -> {
			return new PersonnelDTO(personnel);
		};
	}

	@Override
	protected Function<? super PersonnelDTO, ? extends Personnel> getFromDTOFunction() {
		return dto -> {
			return dto.getPersistenceModel(stationRepository);
		};
	}

	@Override
	protected String getId(Personnel t) {
		return t.getId();
	}

	@Override
	protected void beforeCreate(Personnel entity) {
		entity.getHome().ifPresent(home -> {
			if (home.getCoordinate() == null || home.getCoordinate().getLatitude() == 0d
					&& home.getCoordinate().getLongitude() == 0d) {
				setCoordinate(home);
			}
		});
	}

	private void setCoordinate(Location location) {
		geoServices.geocoding().ifPresent(geocoding -> {
			geocoding.geocode(location);
		});
	}
}
