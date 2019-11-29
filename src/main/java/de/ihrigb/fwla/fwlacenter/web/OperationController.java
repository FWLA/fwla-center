package de.ihrigb.fwla.fwlacenter.web;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.ihrigb.fwla.fwlacenter.handling.api.OperationChain;
import de.ihrigb.fwla.fwlacenter.persistence.model.Operation;
import de.ihrigb.fwla.fwlacenter.persistence.repository.OperationKeyRepository;
import de.ihrigb.fwla.fwlacenter.persistence.repository.OperationRepository;
import de.ihrigb.fwla.fwlacenter.persistence.repository.RealEstateRepository;
import de.ihrigb.fwla.fwlacenter.persistence.repository.ResourceRepository;
import de.ihrigb.fwla.fwlacenter.persistence.repository.StationRepository;
import de.ihrigb.fwla.fwlacenter.services.api.OperationService;
import de.ihrigb.fwla.fwlacenter.web.model.OperationDTO;

@Transactional
@RestController
@RequestMapping("/v1/operations")
public class OperationController extends BaseController<Operation, String, OperationDTO, OperationRepository> {

	private final OperationRepository operationRepository;
	private final OperationService operationService;
	private final OperationChain operationChain;
	private final OperationKeyRepository operationKeyRepository;
	private final RealEstateRepository realEstateRepository;
	private final ResourceRepository resourceRepository;
	private final StationRepository stationRepository;

	public OperationController(OperationRepository operationRepository, OperationService operationService,
			OperationChain operationChain, OperationKeyRepository operationKeyRepository,
			RealEstateRepository realEstateRepository, ResourceRepository resourceRepository,
			StationRepository stationRepository) {
		super(operationRepository);
		this.operationRepository = operationRepository;
		this.operationService = operationService;
		this.operationChain = operationChain;
		this.operationKeyRepository = operationKeyRepository;
		this.realEstateRepository = realEstateRepository;
		this.resourceRepository = resourceRepository;
		this.stationRepository = stationRepository;
	}

	@GetMapping
	public ResponseEntity<?> getOperations(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "sort", required = false) String sort) {
		return super.doGetAll(page, size, null, sort);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getOne(@PathVariable("id") String id) {
		return super.doGetOne(id);
	}

	@PostMapping("/all")
	public ResponseEntity<?> importAll(@RequestBody List<Operation> operations) {
		operationRepository.saveAll(operations);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/stations/{stationId}/active")
	public ResponseEntity<?> getActiveOperation(@PathVariable("stationId") String stationId) {
		return stationRepository.findById(stationId).map(station -> {
			return operationService.getActiveOperation(station).map(getToDTOFunction()).map(oDTO -> {
				return ResponseEntity.ok(oDTO);
			}).orElseGet(() -> {
				return ResponseEntity.notFound().build();
			});
		}).orElseGet(() -> {
			return ResponseEntity.notFound().build();
		});
	}

	@GetMapping("/stations/{stationId}/current")
	public ResponseEntity<?> getCurrentOperations(@PathVariable("stationId") String stationId) {
		return stationRepository.findById(stationId).map(station -> {
			return ResponseEntity.ok(operationService.getCurrentOperations(station).stream().map(getToDTOFunction())
					.collect(Collectors.toList()));
		}).orElseGet(() -> {
			return ResponseEntity.notFound().build();
		});
	}

	@PostMapping
	public ResponseEntity<?> createTraining(@RequestBody OperationDTO dto) {
		Operation operation = getFromDTOFunction().apply(dto);
		operation.setId("training-" + UUID.randomUUID().toString());
		operation.setTraining(true);
		operationChain.put(operation);
		return ResponseEntity.accepted().build();
	}

	@PatchMapping("/{id}/close")
	public ResponseEntity<?> close(@PathVariable("id") String id) {
		operationService.closeOperation(id);
		return ResponseEntity.accepted().build();
	}

	@Override
	protected Function<? super Operation, ? extends OperationDTO> getToDTOFunction() {
		return operation -> operation == null ? null : new OperationDTO(operation);
	}

	@Override
	protected Function<? super OperationDTO, ? extends Operation> getFromDTOFunction() {
		return dto -> dto.getPersistenceModel(operationKeyRepository, realEstateRepository, resourceRepository);
	}

	@Override
	protected String getId(Operation t) {
		return t.getId();
	}
}
