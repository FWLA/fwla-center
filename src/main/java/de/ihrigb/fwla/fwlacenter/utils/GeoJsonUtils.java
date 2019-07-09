package de.ihrigb.fwla.fwlacenter.utils;

import java.util.function.Function;
import java.util.stream.Collector;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.LngLatAlt;
import org.geojson.Point;

import de.ihrigb.fwla.fwlacenter.api.Coordinate;

public final class GeoJsonUtils {

	public static Coordinate fromLngLatAlt(LngLatAlt lngLatAlt) {
		return new Coordinate(lngLatAlt.getLatitude(), lngLatAlt.getLongitude());
	}

	public static LngLatAlt toLngLatAlt(Coordinate coordinate) {
		return new LngLatAlt(coordinate.getLongitude(), coordinate.getLatitude());
	}

	public static Point toPoint(Coordinate coordinate) {
		return new Point(GeoJsonUtils.toLngLatAlt(coordinate));
	}

	public static Collector<Feature, ?, FeatureCollection> collector() {
		return Collector.of(() -> new FeatureCollection(),
				(featureCollection, feature) -> featureCollection.add(feature),
				(featureCollection1, featureCollection2) -> {
					featureCollection1.addAll(featureCollection2.getFeatures());
					return featureCollection1;
				}, Function.identity(), Collector.Characteristics.CONCURRENT, Collector.Characteristics.IDENTITY_FINISH,
				Collector.Characteristics.UNORDERED);
	}

	private GeoJsonUtils() {
	}
}
