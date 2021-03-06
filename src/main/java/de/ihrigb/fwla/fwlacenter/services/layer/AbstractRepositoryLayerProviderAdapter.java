package de.ihrigb.fwla.fwlacenter.services.layer;

import java.io.Serializable;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.GeoJsonObject;
import org.springframework.data.jpa.repository.JpaRepository;

import de.ihrigb.fwla.fwlacenter.api.Locatable;
import de.ihrigb.fwla.fwlacenter.utils.GeoJsonUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
abstract class AbstractRepositoryLayerProviderAdapter<T extends Locatable, ID extends Serializable> extends AbstractLayerProvider {

	private final JpaRepository<T, ID> repository;

	abstract String getId(T t);

	abstract String getName(T t);

	abstract Feature toFeature(T t);

	protected FeatureCollection getFeatures() {
		return repository.findAll().stream().map(t -> toFeature(t)).collect(GeoJsonUtils.collector());
	}

	@Override
	public FeatureCollection getFeatures(String layer) {
		if (getLayerGroups().stream().findFirst().map(layerGroup -> {
			return layerGroup.getLayers().stream().findFirst().map(l -> {
				return layer.equals(l.getId());
			}).orElse(false);
		}).orElse(false)) {
			return getFeatures();
		}
		return new FeatureCollection();
	}

	protected JpaRepository<T, ID> getRepository() {
		return repository;
	}

	protected Feature createFeature(T t, GeoJsonObject geometry, String color) {
		Feature feature = createFeature(getId(t), geometry, true);
		setNameProperty(feature, getName(t));
		setColorProperty(feature, color);
		return feature;
	}
}
