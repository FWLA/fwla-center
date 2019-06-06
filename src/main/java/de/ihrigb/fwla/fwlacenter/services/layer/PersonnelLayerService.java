package de.ihrigb.fwla.fwlacenter.services.layer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import de.ihrigb.fwla.fwlacenter.api.Address;
import de.ihrigb.fwla.fwlacenter.persistence.model.Personnel;
import de.ihrigb.fwla.fwlacenter.persistence.repository.PersonnelRepository;
import de.ihrigb.fwla.fwlacenter.services.api.geo.Feature;
import de.ihrigb.fwla.fwlacenter.services.api.geo.FeatureDetails;
import de.ihrigb.fwla.fwlacenter.services.api.geo.Layer;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerGroup;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerService;
import de.ihrigb.fwla.fwlacenter.services.api.geo.PointFeature;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PersonnelLayerService implements LayerService {

	private static final String homeLayerName = "personnel_home";
	private static final String workLayerName = "personnel_home";
	private static final String homePrefix = "personnel_home_";
	private static final String workPrefix = "personnel_work_";

	private final PersonnelRepository personnelRepository;

	@Override
	public List<LayerGroup> getLayerGroups() {
		Layer homeLayer = new Layer(homeLayerName, "Personal Wohnort");
		Layer workLayer = new Layer(workLayerName, "Personal Arbeitsplatz");
		return Collections.singletonList(new LayerGroup("personnel", Arrays.asList(homeLayer, workLayer)));
	}

	@Override
	public Set<? extends Feature> getFeatures(String layer) {
		Function<Personnel, PointFeature> toFeature;
		Predicate<Personnel> filter;
		if (homeLayerName.equals(layer)) {
			toFeature = toHomePointFeature();
			filter = p -> p.getHome().isPresent();
		} else if (workLayerName.equals(layer)) {
			toFeature = toWorkPointFeature();
			filter = p -> p.getWork().isPresent();
		} else {
			return Collections.emptySet();
		}

		return personnelRepository.findAll().stream().filter(filter).map(toFeature).collect(Collectors.toSet());
	}

	@Override
	public Optional<FeatureDetails> getFeatureDetails(String layer, String featureId) {
		if (!homeLayerName.equals(layer) || !workLayerName.equals(layer)) {
			return Optional.empty();
		}
		String id;
		Function<Personnel, Address> getAddress;
		if (featureId.startsWith(homePrefix)) {
			id = featureId.substring(homePrefix.length());
			getAddress = p -> p.getHome().map(h -> h.getAddress()).orElse(null);
		} else if (featureId.startsWith(workPrefix)) {
			id = featureId.substring(workPrefix.length());
			getAddress = p -> p.getWork().map(w -> w.getAddress()).orElse(null);
		} else {
			return Optional.empty();
		}

		return personnelRepository.findById(id).map(p -> {
			return new FeatureDetails(p.getName(), null, getAddress.apply(p));
		});
	}

	private Function<Personnel, PointFeature> toHomePointFeature() {
		return personnel -> {
			return new PointFeature(homePrefix + personnel.getId(), "Wohnort " + personnel.getName(),
					personnel.getHome().map(h -> h.getCoordinate()).orElse(null), "grey");
		};
	}

	private Function<Personnel, PointFeature> toWorkPointFeature() {
		return personnel -> {
			return new PointFeature(workPrefix + personnel.getId(), "Arbeitsplatz " + personnel.getName(),
					personnel.getWork().map(w -> w.getCoordinate()).orElse(null), "grey");
		};
	}
}
