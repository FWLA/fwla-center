package de.ihrigb.fwla.fwlacenter.mail.parse;

import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.ihrigb.fwla.fwlacenter.persistence.model.RegexPattern;
import de.ihrigb.fwla.fwlacenter.persistence.model.Source;
import de.ihrigb.fwla.fwlacenter.persistence.repository.RegexPatternRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RestController
@RequestMapping("/v1/patterns")
@RequiredArgsConstructor
public class RegexPatternController {

	private final RegexPatternRepository repository;

	@GetMapping
	public ResponseEntity<?> getAll() {
		return ResponseEntity.ok(repository.findAll().stream().map(regexPattern -> new RegexPatternDTO(regexPattern))
				.collect(Collectors.toList()));
	}

	@GetMapping("/{name}")
	public ResponseEntity<?> getOne(@PathVariable("name") String name) {
		return repository.findById(name).map(regexPattern -> {
			return ResponseEntity.ok(new RegexPatternDTO(regexPattern));
		}).orElse(ResponseEntity.notFound().build());
	}

	@PatchMapping("/{name}")
	public ResponseEntity<?> update(@PathVariable("name") String name, @Valid @RequestBody UpdateRegexPatternDTO dto) {
		return repository.findById(name).map(regexPattern -> {
			dto.update(regexPattern);
			regexPattern = repository.save(regexPattern);
			return ResponseEntity.ok(new RegexPatternDTO(regexPattern));
		}).orElse(ResponseEntity.notFound().build());
	}

	@Getter
	@Setter
	static class UpdateRegexPatternDTO {
		private String pattern;
		private Source source;

		void update(RegexPattern regexPattern) {
			regexPattern.setPattern(pattern);
			regexPattern.setSource(source);
		}
	}

	@Getter
	static class RegexPatternDTO {
		private final String fieldName;
		private final String pattern;
		private final Source source;

		RegexPatternDTO(RegexPattern regexPattern) {
			this.fieldName = regexPattern.getFieldName();
			this.pattern = regexPattern.getPattern();
			this.source = regexPattern.getSource();
		}
	}
}
