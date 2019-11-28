package de.ihrigb.fwla.fwlacenter.services.geoservices;

import java.util.Optional;

import org.geojson.FeatureCollection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.ihrigb.fwla.fwlacenter.api.Coordinate;
import de.ihrigb.fwla.fwlacenter.services.api.DirectionsService;
import de.ihrigb.fwla.fwlacenter.services.geoservices.GeoServiceProperties.CacheProperties;

public class DirectionsServiceDelegateCacheTest {

	private DirectionsServiceDelegateCache testee;

	private DirectionsService delegate;

	@Before
	public void setUp() throws Exception {
		delegate = Mockito.mock(DirectionsService.class);
		testee = new DirectionsServiceDelegateCache(delegate, new CacheProperties());
	}

	@Test
	public void testCaching() throws Exception {
		Optional<FeatureCollection> fOptional = Optional.of(new FeatureCollection());
		Mockito.when(delegate.getDirections(Mockito.any(), Mockito.any())).thenReturn(fOptional);

		Assert.assertSame(fOptional, testee.getDirections(new Coordinate(2.0, 2.0), new Coordinate(3.0, 3.0)));
		Assert.assertSame(fOptional, testee.getDirections(new Coordinate(2.0, 2.0), new Coordinate(3.0, 3.0)));

		Mockito.verify(delegate, Mockito.times(1)).getDirections(Mockito.any(), Mockito.any());
	}
}
