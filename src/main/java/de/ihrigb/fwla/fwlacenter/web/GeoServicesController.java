package de.ihrigb.fwla.fwlacenter.web;

import org.geojson.FeatureCollection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.ihrigb.fwla.fwlacenter.services.api.GeoServices;
import de.ihrigb.fwla.fwlacenter.web.model.CoordinateDTO;
import de.ihrigb.fwla.fwlacenter.web.model.DirectionsRequestDTO;
import lombok.RequiredArgsConstructor;

@Transactional
@RestController
@RequestMapping("/v1/services/geo")
@RequiredArgsConstructor
public class GeoServicesController {

	private final GeoServices geoServices;

	@GetMapping("/geocode")
	public ResponseEntity<CoordinateDTO> geocode(@RequestParam(required = true, name = "query") String query) {
		return geoServices.geocoding().map(geocode -> geocode.geocode(query).map(coordinate -> {
			return ResponseEntity.ok(new CoordinateDTO(coordinate));
		}).orElseGet(() -> {
			return ResponseEntity.notFound().build();
		})).orElseGet(() -> {
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
		});
	}

	@PostMapping("/directions")
	public ResponseEntity<FeatureCollection> directions(@RequestBody DirectionsRequestDTO directionsRequest) {
		return geoServices.directions().map(directions -> {
			return directions
					.getDirections(directionsRequest.getFrom().getApiModel(), directionsRequest.getTo().getApiModel())
					.map(featureCollection -> {
						return ResponseEntity.ok(featureCollection);
					}).orElseGet(() -> {
						return ResponseEntity.notFound().build();
					});
		}).orElseGet(() -> {
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
		});
	}
}
