package de.ihrigb.fwla.fwlacenter.services.river;

import java.net.URI;
import java.util.List;
import java.util.Locale;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import de.ihrigb.fwla.fwlacenter.services.river.model.BWaStrInfoQuery;
import de.ihrigb.fwla.fwlacenter.services.river.model.BWaStrInfoResult;
import de.ihrigb.fwla.fwlacenter.services.river.model.FehlkilometerQuery;
import de.ihrigb.fwla.fwlacenter.services.river.model.GeocodierungQuery;
import de.ihrigb.fwla.fwlacenter.services.river.model.GeocodierungResult;
import de.ihrigb.fwla.fwlacenter.services.river.model.RootQuery;
import de.ihrigb.fwla.fwlacenter.services.river.model.RootResult;
import de.ihrigb.fwla.fwlacenter.services.river.model.BWaStrInfoQuery.SearchField;
import de.ihrigb.fwla.fwlacenter.services.river.model.GeocodierungQuery.SpatialReference;

public class WSVRestServiceClient {

	private static final String baseUri = "http://atlas.wsv.bund.de/bwastr-locator/rest";
	private static final String bWaStrInfoUri = baseUri + "/bwastrinfo/query";
	private static final String fehlkilometerUri = baseUri + "/fehlkilometer/query";
	private static final String geocodierungUri = baseUri + "/geokodierung/query";

	private final RestTemplate restTemplate;

	protected WSVRestServiceClient(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public WSVRestServiceClient(RestTemplateBuilder restTemplateBuilder, WSVRestClientProperties properties) {
		this.restTemplate = restTemplateBuilder.setConnectTimeout(properties.getConnectTimeout())
				.setReadTimeout(properties.getReadTimeout()).build();
	}

	public RootResult<BWaStrInfoResult> bWaStrInfo(String searchTerm) {
		return bWaStrInfo(searchTerm, SearchField.all);
	}

	public RootResult<BWaStrInfoResult> bWaStrInfo(String searchTerm, SearchField searchField) {

		URI uri = UriComponentsBuilder.fromUriString(bWaStrInfoUri).queryParam("searchterm", searchTerm)
				.queryParam("searchfield", searchField.name()).build().toUri();

		ParameterizedTypeReference<RootResult<BWaStrInfoResult>> type = new ParameterizedTypeReference<RootResult<BWaStrInfoResult>>() {
		};

		return restTemplate.exchange(uri, HttpMethod.GET, null, type).getBody();
	}

	public RootResult<BWaStrInfoResult> bWaStrInfo(List<BWaStrInfoQuery> queries) {

		URI uri = UriComponentsBuilder.fromUriString(bWaStrInfoUri).build().toUri();

		ParameterizedTypeReference<RootResult<BWaStrInfoResult>> type = new ParameterizedTypeReference<RootResult<BWaStrInfoResult>>() {
		};

		RootQuery<BWaStrInfoQuery> query = new RootQuery<>();
		query.setQueries(queries);
		HttpEntity<RootQuery<BWaStrInfoQuery>> httpEntity = new HttpEntity<RootQuery<BWaStrInfoQuery>>(query);

		return restTemplate.exchange(uri, HttpMethod.POST, httpEntity, type).getBody();
	}

	public RootResult<BWaStrInfoResult> fehlkilometer(String bWaStrId) {
		return fehlkilometer(bWaStrId, null, null);
	}

	public RootResult<BWaStrInfoResult> fehlkilometer(String bWaStrId, Float kmVon, Float kmBis) {

		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(fehlkilometerUri).queryParam("bwastrid",
				bWaStrId);
		if (kmVon != null && kmVon >= 0) {
			uriBuilder = uriBuilder.queryParam("km_von", String.format(Locale.US, "%f", kmVon));
		}
		if (kmBis != null && kmBis >= 0) {
			uriBuilder = uriBuilder.queryParam("km_bis", String.format(Locale.US, "%f", kmBis));
		}

		URI uri = uriBuilder.build().toUri();

		ParameterizedTypeReference<RootResult<BWaStrInfoResult>> type = new ParameterizedTypeReference<RootResult<BWaStrInfoResult>>() {
		};

		return restTemplate.exchange(uri, HttpMethod.GET, null, type).getBody();
	}

	public RootResult<BWaStrInfoResult> fehlkilometer(List<FehlkilometerQuery> queries) {

		URI uri = UriComponentsBuilder.fromUriString(fehlkilometerUri).build().toUri();

		ParameterizedTypeReference<RootResult<BWaStrInfoResult>> type = new ParameterizedTypeReference<RootResult<BWaStrInfoResult>>() {
		};

		RootQuery<FehlkilometerQuery> query = new RootQuery<>();
		query.setQueries(queries);
		HttpEntity<RootQuery<FehlkilometerQuery>> httpEntity = new HttpEntity<RootQuery<FehlkilometerQuery>>(query);

		return restTemplate.exchange(uri, HttpMethod.POST, httpEntity, type).getBody();
	}

	public RootResult<GeocodierungResult> geocode(String bWaStrId, float km) {
		return geocode(bWaStrId, km, 0);
	}

	public RootResult<GeocodierungResult> geocode(String bWaStrId, float km, float offset) {

		URI uri = UriComponentsBuilder.fromUriString(geocodierungUri).queryParam("bwastrid", bWaStrId)
				.queryParam("km_wert", String.format(Locale.US, "%f", km))
				.queryParam("offset", String.format(Locale.US, "%f", offset)).queryParam("wkid", SpatialReference.WGS84)
				.build().toUri();

		ParameterizedTypeReference<RootResult<GeocodierungResult>> type = new ParameterizedTypeReference<RootResult<GeocodierungResult>>() {
		};

		return restTemplate.exchange(uri, HttpMethod.GET, null, type).getBody();
	}

	public RootResult<GeocodierungResult> geocode(List<GeocodierungQuery> queries) {

		URI uri = UriComponentsBuilder.fromUriString(geocodierungUri).build().toUri();

		ParameterizedTypeReference<RootResult<GeocodierungResult>> type = new ParameterizedTypeReference<RootResult<GeocodierungResult>>() {
		};

		RootQuery<GeocodierungQuery> query = new RootQuery<>();
		query.setQueries(queries);
		HttpEntity<RootQuery<GeocodierungQuery>> httpEntity = new HttpEntity<RootQuery<GeocodierungQuery>>(query);

		return restTemplate.exchange(uri, HttpMethod.POST, httpEntity, type).getBody();
	}
}
