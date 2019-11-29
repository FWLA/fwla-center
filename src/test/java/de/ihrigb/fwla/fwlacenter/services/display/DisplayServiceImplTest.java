package de.ihrigb.fwla.fwlacenter.services.display;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.info.BuildProperties;

import de.ihrigb.fwla.fwlacenter.api.Coordinate;
import de.ihrigb.fwla.fwlacenter.api.Location;
import de.ihrigb.fwla.fwlacenter.persistence.model.DisplayEvent;
import de.ihrigb.fwla.fwlacenter.persistence.model.Operation;
import de.ihrigb.fwla.fwlacenter.persistence.model.Station;
import de.ihrigb.fwla.fwlacenter.persistence.repository.DisplayEventRepository;
import de.ihrigb.fwla.fwlacenter.services.api.DisplayState;
import de.ihrigb.fwla.fwlacenter.services.api.DisplayState.State;
import de.ihrigb.fwla.fwlacenter.services.api.OperationService;
import de.ihrigb.fwla.fwlacenter.services.api.Weather;
import de.ihrigb.fwla.fwlacenter.services.api.WeatherService;

public class DisplayServiceImplTest {

	private DisplayServiceImpl testee;

	private BuildProperties buildProperties;
	private OperationService operationService;
	private WeatherService weatherService;
	private DisplayEventRepository displayEventRepository;

	@Before
	public void setUp() throws Exception {
		buildProperties = mock(BuildProperties.class);
		operationService = mock(OperationService.class);
		weatherService = mock(WeatherService.class);
		displayEventRepository = mock(DisplayEventRepository.class);

		testee = new DisplayServiceImpl(Optional.of(buildProperties), operationService, Optional.of(weatherService),
				displayEventRepository);
	}

	@Test
	public void testNoOperationNoDisplayEvent() throws Exception {

		Coordinate stationCoordinate = new Coordinate(2.0, 3.0);
		Location stationLocation = new Location();
		stationLocation.setCoordinate(stationCoordinate);
		Station station = new Station();
		station.setLocation(stationLocation);

		when(buildProperties.getVersion()).thenReturn("0.1.2");
		when(displayEventRepository.getActive()).thenReturn(Optional.empty());
		when(operationService.getActiveOperation(station)).thenReturn(Optional.empty());
		Weather weather = mock(Weather.class);
		when(weatherService.getWeather(stationCoordinate)).thenReturn(weather);

		DisplayState displayState = testee.getDisplayState(station);

		assertSame(stationCoordinate, displayState.getHome().get());
		assertFalse(displayState.getOperation().isPresent());
		assertEquals("0.1.2", displayState.getServerVersion());
		assertEquals(State.IDLE, displayState.getState());
		assertFalse(displayState.getText().isPresent());
		assertSame(weather, displayState.getWeather().get());
	}

	@Test
	public void testOperationNoDisplayEvent() throws Exception {

		Coordinate stationCoordinate = new Coordinate(2.0, 3.0);
		Location stationLocation = new Location();
		stationLocation.setCoordinate(stationCoordinate);
		Station station = new Station();
		station.setLocation(stationLocation);

		when(buildProperties.getVersion()).thenReturn("0.1.2");
		when(displayEventRepository.getActive()).thenReturn(Optional.empty());
		Operation operation = mock(Operation.class);
		Location location = new Location();
		location.setCoordinate(new Coordinate(1.2d, 1.3d));
		when(operation.getLocation()).thenReturn(location);
		when(operationService.getActiveOperation(station)).thenReturn(Optional.of(operation));
		Weather weather = mock(Weather.class);
		when(weatherService.getWeather(location.getCoordinate())).thenReturn(weather);

		DisplayState displayState = testee.getDisplayState(station);

		assertSame(stationCoordinate, displayState.getHome().get());
		assertSame(operation, displayState.getOperation().get());
		assertEquals("0.1.2", displayState.getServerVersion());
		assertEquals(State.OPERATION, displayState.getState());
		assertFalse(displayState.getText().isPresent());
		assertSame(weather, displayState.getWeather().get());
	}

	@Test
	public void testOperationAndDisplayEventShowOperation() throws Exception {

		Coordinate stationCoordinate = new Coordinate(2.0, 3.0);
		Location stationLocation = new Location();
		stationLocation.setCoordinate(stationCoordinate);
		Station station = new Station();
		station.setLocation(stationLocation);

		when(buildProperties.getVersion()).thenReturn("0.1.2");
		DisplayEvent displayEvent = mock(DisplayEvent.class);
		when(displayEvent.isShowOperation()).thenReturn(true);
		when(displayEventRepository.getActive()).thenReturn(Optional.of(displayEvent));
		Operation operation = mock(Operation.class);
		Location location = new Location();
		location.setCoordinate(new Coordinate(1.2d, 1.3d));
		when(operation.getLocation()).thenReturn(location);
		when(operationService.getActiveOperation(station)).thenReturn(Optional.of(operation));
		Weather weather = mock(Weather.class);
		when(weatherService.getWeather(location.getCoordinate())).thenReturn(weather);

		DisplayState displayState = testee.getDisplayState(station);

		assertSame(stationCoordinate, displayState.getHome().get());
		assertSame(operation, displayState.getOperation().get());
		assertEquals("0.1.2", displayState.getServerVersion());
		assertEquals(State.OPERATION, displayState.getState());
		assertFalse(displayState.getText().isPresent());
		assertSame(weather, displayState.getWeather().get());
	}

	@Test
	public void testOperationAndDisplayEventNotShowOperation() throws Exception {

		Coordinate stationCoordinate = new Coordinate(2.0, 3.0);
		Location stationLocation = new Location();
		stationLocation.setCoordinate(stationCoordinate);
		Station station = new Station();
		station.setLocation(stationLocation);

		when(buildProperties.getVersion()).thenReturn("0.1.2");
		DisplayEvent displayEvent = mock(DisplayEvent.class);
		when(displayEvent.isShowOperation()).thenReturn(false);
		when(displayEvent.getText()).thenReturn("<h1>My Header</h1>");
		when(displayEventRepository.getActive()).thenReturn(Optional.of(displayEvent));
		Operation operation = mock(Operation.class);
		Location location = new Location();
		location.setCoordinate(new Coordinate(1.2d, 1.3d));
		when(operation.getLocation()).thenReturn(location);
		when(operationService.getActiveOperation(station)).thenReturn(Optional.of(operation));
		Weather weather = mock(Weather.class);
		when(weatherService.getWeather(stationCoordinate)).thenReturn(weather);

		DisplayState displayState = testee.getDisplayState(station);

		assertSame(stationCoordinate, displayState.getHome().get());
		assertFalse(displayState.getOperation().isPresent());
		assertEquals("0.1.2", displayState.getServerVersion());
		assertEquals(State.TEXT, displayState.getState());
		assertEquals("<h1>My Header</h1>", displayState.getText().get());
		assertSame(weather, displayState.getWeather().get());
	}

	@Test
	public void testNoOperationDisplayEvent() throws Exception {

		Coordinate stationCoordinate = new Coordinate(2.0, 3.0);
		Location stationLocation = new Location();
		stationLocation.setCoordinate(stationCoordinate);
		Station station = new Station();
		station.setLocation(stationLocation);

		when(buildProperties.getVersion()).thenReturn("0.1.2");
		DisplayEvent displayEvent = mock(DisplayEvent.class);
		when(displayEvent.getText()).thenReturn("<h1>My Header</h1>");
		when(displayEventRepository.getActive()).thenReturn(Optional.of(displayEvent));
		when(operationService.getActiveOperation(station)).thenReturn(Optional.empty());
		Weather weather = mock(Weather.class);
		when(weatherService.getWeather(stationCoordinate)).thenReturn(weather);

		DisplayState displayState = testee.getDisplayState(station);

		assertSame(stationCoordinate, displayState.getHome().get());
		assertFalse(displayState.getOperation().isPresent());
		assertEquals("0.1.2", displayState.getServerVersion());
		assertEquals(State.TEXT, displayState.getState());
		assertEquals("<h1>My Header</h1>", displayState.getText().get());
		assertSame(weather, displayState.getWeather().get());
	}
}
