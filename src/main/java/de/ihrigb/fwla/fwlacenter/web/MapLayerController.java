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

import de.ihrigb.fwla.fwlacenter.persistence.model.MapLayer;
import de.ihrigb.fwla.fwlacenter.persistence.repository.MapLayerRepository;
import de.ihrigb.fwla.fwlacenter.web.model.MapLayerDTO;

@Transactional
@RestController
@RequestMapping("/v1/mapLayers")
public class MapLayerController extends BaseController<MapLayer, String, MapLayerDTO, MapLayerRepository> {

	public MapLayerController(MapLayerRepository mapLayerRepository) {
		super(mapLayerRepository);
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
	public ResponseEntity<?> create(@RequestBody MapLayerDTO dto) {
		return super.doCreate(dto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable("id") String id, @RequestBody MapLayerDTO dto) {
		return super.doUpdate(id, dto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") String id) {
		return super.doDelete(id);
	}

	@Override
	protected String getId(MapLayer t) {
		return t.getId();
	}

	@Override
	protected Function<? super MapLayerDTO, ? extends MapLayer> getFromDTOFunction() {
		return t -> t.getPersistenceModel();
	}

	@Override
	protected Function<? super MapLayer, ? extends MapLayerDTO> getToDTOFunction() {
		return t -> new MapLayerDTO(t);
	}
}
