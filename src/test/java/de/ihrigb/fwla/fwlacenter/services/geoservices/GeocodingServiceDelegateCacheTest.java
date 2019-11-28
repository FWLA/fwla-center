package de.ihrigb.fwla.fwlacenter.services.geoservices;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.ihrigb.fwla.fwlacenter.api.Coordinate;
import de.ihrigb.fwla.fwlacenter.services.api.GeocodingService;

public class GeocodingServiceDelegateCacheTest {

	private GeocodingServiceDelegateCache testee;
	private GeocodingService delegate;

	@Before
	public void setUp() throws Exception {
		delegate = Mockito.mock(GeocodingService.class);
		testee = new GeocodingServiceDelegateCache(delegate, new GeoServiceProperties());
	}

	@Test
	public void testCaching() throws Exception {
		Optional<Coordinate> optionalCoordinate = Optional.of(new Coordinate(2.0, 2.0));
		Mockito.when(delegate.geocode("value")).thenReturn(optionalCoordinate);

		Assert.assertSame(optionalCoordinate, testee.geocode("value"));
		Assert.assertSame(optionalCoordinate, testee.geocode("value"));

		Mockito.verify(delegate, Mockito.times(1)).geocode(Mockito.anyString());
	}
}
