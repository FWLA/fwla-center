package de.ihrigb.fwla.fwlacenter.services.layer;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;

import de.ihrigb.fwla.fwlacenter.api.Locatable;
import de.ihrigb.fwla.fwlacenter.services.api.geo.Feature;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
abstract class AbstractLayerAdapter<T extends Locatable> implements LayerService {
	private final JpaRepository<T, ?> repository;

	abstract String getId(T t);

	abstract String getName(T t);

	abstract Feature toFeature(T t);

	@Override
	public Set<? extends Feature> getFeatures() {
		return repository.findAll().stream().map(t -> toFeature(t)).collect(Collectors.toSet());
	}

	@Override
	public Set<? extends Feature> getFeatures(String layer) {
		if (getLayers().stream().findFirst().map(l -> layer.equals(l.getId())).orElse(false)) {
			return getFeatures();
		}
		return Collections.emptySet();
	}
}
