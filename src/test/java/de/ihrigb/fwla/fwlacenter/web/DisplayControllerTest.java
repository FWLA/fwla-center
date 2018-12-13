package de.ihrigb.fwla.fwlacenter.web;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import de.ihrigb.fwla.fwlacenter.services.api.DisplayService;
import de.ihrigb.fwla.fwlacenter.services.api.DisplayState.State;
import de.ihrigb.fwla.fwlacenter.services.display.BaseDisplayState;

@RunWith(SpringRunner.class)
@WebMvcTest(DisplayController.class)
public class DisplayControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private DisplayService displayService;

	@Test
	public void testGetState() throws Exception {
		when(displayService.getDisplayState()).thenReturn(BaseDisplayState.builder().state(State.IDLE).build());

		mvc.perform(get("/v1/display")).andExpect(status().isOk()).andExpect(content().json("{\"state\":\"IDLE\"}"));

		verify(displayService, times(1)).getDisplayState();
	}
}
