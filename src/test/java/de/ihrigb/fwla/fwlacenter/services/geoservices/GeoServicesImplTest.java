package de.ihrigb.fwla.fwlacenter.services.geoservices;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.ihrigb.fwla.fwlacenter.services.api.DirectionsService;
import de.ihrigb.fwla.fwlacenter.services.api.GeocodingService;

public class GeoServicesImplTest {

	private GeoServicesImpl testee;
	private GeocodingService geocodingService;
	private DirectionsService directionsService;

	@Before
	public void setUp() throws Exception {
		geocodingService = Mockito.mock(GeocodingService.class);
		directionsService = Mockito.mock(DirectionsService.class);
		testee = new GeoServicesImpl(Optional.of(directionsService), Optional.of(geocodingService));
	}

	@Test
	public void testService() throws Exception {
		Assert.assertSame(geocodingService, testee.geocoding().get());
		Assert.assertSame(directionsService, testee.directions().get());
	}
}
