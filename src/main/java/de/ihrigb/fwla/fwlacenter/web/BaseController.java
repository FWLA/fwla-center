package de.ihrigb.fwla.fwlacenter.web;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.ihrigb.fwla.fwlacenter.web.model.DataResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
abstract class BaseController<T, ID extends Serializable, DTO, Repository extends JpaRepository<T, ID>> {

	private static final Pattern sortPattern = Pattern
			.compile("\\[\\\"(?<property>[\\w\\d]+)\\\"\\,\\\"(?<direction>ASC|DESC)\\\"\\]");

	private static Optional<Sort> handleSort(String sort) {
		return Optional.ofNullable(sort).filter(s -> {
			return sortPattern.matcher(s).matches();
		}).map(s -> {
			Matcher m = sortPattern.matcher(s);
			m.matches();
			return Sort.by(Direction.fromString(m.group("direction")), m.group("property"));
		});
	}

	@Getter(AccessLevel.PROTECTED)
	private final Repository repository;
	private final ObjectMapper objectMapper = new ObjectMapper();

	protected BaseController(Repository repository) {
		this.repository = repository;
	}

	protected ResponseEntity<DataResponse<List<DTO>>> doGetAll() {
		List<DTO> dtos = repository.findAll().stream().map(getToDTOFunction()).collect(Collectors.toList());
		return ResponseEntity.ok(new DataResponse<>(dtos, (long) dtos.size()));
	}

	protected ResponseEntity<DataResponse<List<DTO>>> doGetAll(int page, int size, String filter) {
		return doGetAll(page, size, filter, null);
	}

	protected ResponseEntity<DataResponse<List<DTO>>> doGetAll(int page, int size, String filter, String sort) {

		if (filter != null) {
			TypeReference<Map<String, Object>> type = new TypeReference<Map<String, Object>>() {
			};
			try {
				Map<String, Object> filterMap = objectMapper.readValue(filter, type);
				if (!filterMap.isEmpty() && filterMap.containsKey("id")) {
					return doGetByIds(convertToIds(filterMap.get("id")));
				}
			} catch (IOException e) {
				log.warn("Exception while parsing filter query param.", e);
			}
		}

		Pageable pageable = handleSort(sort).map(s -> {
			return PageRequest.of(page - 1, size, s);
		}).orElse(PageRequest.of(page - 1, size));
		Page<T> pageResult = repository.findAll(pageable);

		List<DTO> list = pageResult.getContent().stream().map(getToDTOFunction()).collect(Collectors.toList());

		return ResponseEntity.ok(new DataResponse<>(list, repository.count()));
	}

	protected ResponseEntity<DataResponse<List<DTO>>> doGetByIds(Set<ID> ids) {
		return ResponseEntity.ok(new DataResponse<>(ids.stream().map(id -> {
			ResponseEntity<DataResponse<DTO>> response = doGetOne(id);
			if (response.getStatusCode() != HttpStatus.OK) {
				return null;
			}
			return response.getBody().getData();
		}).filter(Objects::nonNull).collect(Collectors.toList())));
	}

	protected ResponseEntity<DataResponse<DTO>> doGetOne(ID id) {
		DTO dto = repository.findById(id).map(getToDTOFunction()).orElse(null);
		if (dto == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(new DataResponse<>(dto));
	}

	protected ResponseEntity<DataResponse<DTO>> doCreate(DTO dto) {
		T t = getFromDTOFunction().apply(dto);
		beforeCreate(t);
		t = repository.save(t);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build(getId(t));

		return ResponseEntity.created(location).body(new DataResponse<>(getToDTOFunction().apply(t)));
	}

	protected ResponseEntity<DataResponse<DTO>> doUpdate(ID id, DTO dto) {
		T t = getFromDTOFunction().apply(dto);

		if (!id.equals(getId(t)) || !repository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}

		beforeUpdate(t);
		t = repository.save(t);

		return ResponseEntity.ok(new DataResponse<>(getToDTOFunction().apply(t)));
	}

	protected ResponseEntity<?> doDelete(ID id) {
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Set<ID> convertToIds(Object o) {
		if (!(o instanceof Collection)) {
			return Collections.emptySet();
		}
		return new HashSet<>((Collection) o);
	}

	abstract protected Function<? super T, ? extends DTO> getToDTOFunction();

	abstract protected Function<? super DTO, ? extends T> getFromDTOFunction();

	abstract protected ID getId(T t);

	protected void beforeCreate(T entity) {
	}

	protected void beforeUpdate(T entity) {
	}
}
