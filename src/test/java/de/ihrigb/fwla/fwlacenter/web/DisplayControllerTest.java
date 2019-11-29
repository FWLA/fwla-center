package de.ihrigb.fwla.fwlacenter.web;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import de.ihrigb.fwla.fwlacenter.persistence.model.Station;
import de.ihrigb.fwla.fwlacenter.persistence.repository.StationRepository;
import de.ihrigb.fwla.fwlacenter.services.api.DisplayService;
import de.ihrigb.fwla.fwlacenter.services.api.DisplayState;
import de.ihrigb.fwla.fwlacenter.services.api.DisplayState.State;

@RunWith(SpringRunner.class)
@WebMvcTest(DisplayController.class)
public class DisplayControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private DisplayService displayService;
	@MockBean
	private StationRepository stationRepository;

	@Test
	public void testGetState() throws Exception {
		when(stationRepository.findById("stationId")).thenReturn(Optional.of(new Station()));
		when(displayService.getDisplayState(Mockito.any()))
				.thenReturn(DisplayState.builder().state(State.IDLE).build());

		mvc.perform(get("/v1/display/stationId")).andExpect(status().isOk())
				.andExpect(content().json("{\"state\":\"IDLE\"}"));

		verify(displayService, times(1)).getDisplayState(Mockito.any());
	}
}
