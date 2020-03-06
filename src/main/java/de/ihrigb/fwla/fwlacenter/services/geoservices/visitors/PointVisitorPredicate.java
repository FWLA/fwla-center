package de.ihrigb.fwla.fwlacenter.services.geoservices.visitors;

import java.util.function.Predicate;

import org.geojson.Feature;
import org.geojson.GeoJsonObject;
import org.geojson.GeoJsonObjectVisitor;
import org.geojson.Point;

public class PointVisitorPredicate extends GeoJsonObjectVisitor.Adapter<Boolean> implements Predicate<Feature> {

	@Override
	public Boolean visit(Point geoJsonObject) {
		if (geoJsonObject == null) {
			return false;
		}
		return Boolean.TRUE;
	}

	@Override
	public Boolean visit(Feature geoJsonObject) {
		if (geoJsonObject == null) {
			return false;
		}
		GeoJsonObject geometry = geoJsonObject.getGeometry();
		if (geometry == null) {
			return false;
		}
		return geometry.accept(this);
	}

	@Override
	public boolean test(Feature t) {
		if (t == null) {
			return false;
		}
		Boolean res = t.accept(this);
		if (res == null) {
			return false;
		}
		return res.booleanValue();
	}
}
