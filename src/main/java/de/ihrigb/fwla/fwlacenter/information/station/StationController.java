package de.ihrigb.fwla.fwlacenter.information.station;

import java.net.URI;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import de.ihrigb.fwla.fwlacenter.persistence.model.Address;
import de.ihrigb.fwla.fwlacenter.persistence.model.Station;
import de.ihrigb.fwla.fwlacenter.persistence.repository.StationRepository;
import de.ihrigb.fwla.fwlacenter.web.model.AddressDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RestController
@RequestMapping("/v1/stations")
@RequiredArgsConstructor
public class StationController {

	private final StationRepository repository;

	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody CreateUpdateStationDTO dto) {

		Station station = dto.create();

		station = repository.save(station);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build(station.getId());

		return ResponseEntity.created(location).body(station);
	}

	@GetMapping
	public ResponseEntity<?> getAll() {
		return ResponseEntity
				.ok(repository.findAll().stream().map(station -> new StationDTO(station)).collect(Collectors.toList()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getOne(@PathVariable("id") String id) {
		return repository.findById(id).map(station -> {
			return ResponseEntity.ok(new StationDTO(station));
		}).orElse(ResponseEntity.notFound().build());
	}

	@PatchMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable("id") String id, @Valid @RequestBody CreateUpdateStationDTO dto) {
		return repository.findById(id).map(station -> {
			dto.update(station);
			station = repository.save(station);
			return ResponseEntity.ok(new StationDTO(station));
		}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") String id) {
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@Getter
	@Setter
	static class CreateUpdateStationDTO {
		@NotBlank
		private String name;
		private AddressDTO address;

		Station create() {
			Station station = new Station();
			update(station);
			return station;
		}

		void update(Station station) {
			station.setName(name);
			if (station.getAddress() == null) {
				station.setAddress(new Address());
			}
			address.update(station.getAddress());
		}
	}

	@Getter
	static class StationDTO {
		private final String id;
		private final String name;
		private final AddressDTO address;

		StationDTO(Station station) {
			this.id = station.getId();
			this.name = station.getName();
			this.address = new AddressDTO(station.getAddress());
		}
	}
}
