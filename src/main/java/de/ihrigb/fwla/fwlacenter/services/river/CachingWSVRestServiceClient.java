package de.ihrigb.fwla.fwlacenter.services.river;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;

import de.ihrigb.fwla.fwlacenter.services.river.model.BWaStrInfoQuery;
import de.ihrigb.fwla.fwlacenter.services.river.model.BWaStrInfoResult;
import de.ihrigb.fwla.fwlacenter.services.river.model.FehlkilometerQuery;
import de.ihrigb.fwla.fwlacenter.services.river.model.GeocodierungQuery;
import de.ihrigb.fwla.fwlacenter.services.river.model.GeocodierungResult;
import de.ihrigb.fwla.fwlacenter.services.river.model.Query;
import de.ihrigb.fwla.fwlacenter.services.river.model.Result;
import de.ihrigb.fwla.fwlacenter.services.river.model.RootResult;
import de.ihrigb.fwla.fwlacenter.services.river.model.BWaStrInfoQuery.SearchField;
import de.ihrigb.fwla.fwlacenter.services.river.model.GeocodierungQuery.Stationierung;

@Component
@EnableConfigurationProperties(WSVRestClientProperties.class)
public class CachingWSVRestServiceClient extends WSVRestServiceClient {

	private static <K extends Query, V extends Result> Cache<K, V> buildCache(Duration expireAfterWrite) {
		return Caffeine.newBuilder().expireAfterWrite(expireAfterWrite).build();
	}

	private final Cache<BWaStrInfoQuery, BWaStrInfoResult> bWaStrInfoCache;
	private final Cache<FehlkilometerQuery, BWaStrInfoResult> fehlkilometerCache;
	private final Cache<GeocodierungQuery, GeocodierungResult> geocodingCache;

	public CachingWSVRestServiceClient(RestTemplateBuilder restTemplateBuilder, WSVRestClientProperties properties) {
		super(restTemplateBuilder, properties);

		this.bWaStrInfoCache = CachingWSVRestServiceClient.buildCache(properties.getCacheTimeout());
		this.fehlkilometerCache = CachingWSVRestServiceClient.buildCache(properties.getCacheTimeout());
		this.geocodingCache = CachingWSVRestServiceClient.buildCache(properties.getCacheTimeout());
	}

	@Override
	public RootResult<BWaStrInfoResult> bWaStrInfo(String searchTerm) {
		return bWaStrInfo(searchTerm, SearchField.all);
	}

	@Override
	public RootResult<BWaStrInfoResult> bWaStrInfo(String searchTerm, SearchField searchField) {
		BWaStrInfoQuery query = new BWaStrInfoQuery();
		query.setQid(1);
		query.setSearchTerm(searchTerm);
		query.setSearchField(searchField);
		return bWaStrInfo(Collections.singletonList(query));
	}

	@Override
	public RootResult<BWaStrInfoResult> bWaStrInfo(List<BWaStrInfoQuery> queries) {
		return execute(queries, bWaStrInfoCache, q -> super.bWaStrInfo(q));
	}

	@Override
	public RootResult<BWaStrInfoResult> fehlkilometer(String bWaStrId) {
		return fehlkilometer(bWaStrId, null, null);
	}

	@Override
	public RootResult<BWaStrInfoResult> fehlkilometer(String bWaStrId, Float kmVon, Float kmBis) {
		FehlkilometerQuery query = new FehlkilometerQuery();
		query.setBWaStrId(bWaStrId);
		query.setKmVon(kmVon);
		query.setKmBis(kmBis);
		return fehlkilometer(Collections.singletonList(query));
	}

	@Override
	public RootResult<BWaStrInfoResult> fehlkilometer(List<FehlkilometerQuery> queries) {
		return execute(queries, fehlkilometerCache, q -> super.fehlkilometer(q));
	}

	@Override
	public RootResult<GeocodierungResult> geocode(String bWaStrId, float km) {
		GeocodierungQuery query = new GeocodierungQuery();
		query.setBWaStrId(bWaStrId);

		Stationierung stationierung = new Stationierung();
		stationierung.setKmWert(km);
		query.setStationierung(stationierung);

		return geocode(Collections.singletonList(query));
	}

	@Override
	public RootResult<GeocodierungResult> geocode(List<GeocodierungQuery> queries) {
		return execute(queries, geocodingCache, q -> super.geocode(q));
	}

	private <T extends Query> T getKey(List<T> queries, int qid) {
		return queries.stream().filter(query -> qid == query.getQid()).findFirst().orElse(null);
	}

	private <Q extends Query, R extends Result> RootResult<R> execute(List<Q> queries, Cache<Q, R> cache,
			Function<List<Q>, RootResult<R>> backendQuery) {

		RootResult<R> rootResult = new RootResult<>();
		List<R> result = new ArrayList<>();
		List<Q> open = new ArrayList<>();

		queries.forEach(query -> {
			R res = cache.getIfPresent(query);
			if (res == null) {
				open.add(query);
			} else {
				res.setQid(query.getQid());
				result.add(res);
			}
		});

		List<R> restResult = backendQuery.apply(open).getResult();
		restResult.forEach(res -> {
			cache.put(getKey(open, res.getQid()), res);
			result.add(res);
		});

		rootResult.setResult(result);

		return rootResult;
	}
}
