package de.ihrigb.fwla.fwlacenter.services.geoservices.visitors;

import java.util.function.Function;

import org.geojson.Feature;
import org.geojson.GeoJsonObject;
import org.geojson.GeoJsonObjectVisitor;
import org.geojson.LngLatAlt;
import org.geojson.Point;

import de.ihrigb.fwla.fwlacenter.api.Coordinate;

public class ToCoordinateFunction extends GeoJsonObjectVisitor.Adapter<Coordinate> implements Function<Feature, Coordinate> {

	@Override
	public Coordinate visit(Point geoJsonObject) {
		LngLatAlt coords = geoJsonObject.getCoordinates();
		return new Coordinate(coords.getLatitude(), coords.getLongitude());
	}

	@Override
	public Coordinate visit(Feature geoJsonObject) {
		if (geoJsonObject == null) {
			return null;
		}

		GeoJsonObject geometry = geoJsonObject.getGeometry();
		if (geometry == null) {
			return null;
		}

		return geometry.accept(this);
	}

	@Override
	public Coordinate apply(Feature t) {
		if (t == null) {
			return null;
		}
		return t.accept(this);
	}
}
