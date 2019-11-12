package de.ihrigb.fwla.fwlacenter.utils;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collector;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.LngLatAlt;
import org.geojson.Point;

import de.ihrigb.fwla.fwlacenter.api.Coordinate;

public final class GeoJsonUtils {

	public static Function<Coordinate, LngLatAlt> TO_LNG_LAT_ALT = coordinate -> {
		return new LngLatAlt(coordinate.getLongitude(), coordinate.getLatitude());
	};

	public static Function<LngLatAlt, Coordinate> FROM_LNG_LAT_ALT = lngLatAlt -> {
		return new Coordinate(lngLatAlt.getLatitude(), lngLatAlt.getLongitude());
	};

	public static Function<Coordinate, Point> TO_POINT = coordinate -> {
		return new Point(GeoJsonUtils.toLngLatAlt(coordinate));
	};

	public static Coordinate fromLngLatAlt(LngLatAlt lngLatAlt) {
		return FROM_LNG_LAT_ALT.apply(lngLatAlt);
	}

	public static Optional<Coordinate> fromLngLatAlt(Optional<LngLatAlt> lngLatAlt) {
		return lngLatAlt.map(FROM_LNG_LAT_ALT);
	}

	public static LngLatAlt toLngLatAlt(Coordinate coordinate) {
		return TO_LNG_LAT_ALT.apply(coordinate);
	}

	public static Optional<LngLatAlt> toLngLatAlt(Optional<Coordinate> coordinate) {
		return coordinate.map(TO_LNG_LAT_ALT);
	}

	public static Point toPoint(Coordinate coordinate) {
		return TO_POINT.apply(coordinate);
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
