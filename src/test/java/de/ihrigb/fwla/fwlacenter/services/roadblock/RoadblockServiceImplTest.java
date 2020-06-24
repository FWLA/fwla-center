package de.ihrigb.fwla.fwlacenter.services.roadblock;

import java.util.Collections;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.ihrigb.fwla.fwlacenter.api.Coordinate;
import de.ihrigb.fwla.fwlacenter.persistence.model.Roadblock;
import de.ihrigb.fwla.fwlacenter.persistence.repository.RoadblockRepository;

public class RoadblockServiceImplTest {

	private RoadblockServiceImpl testee;

	private RoadblockRepository roadblockRepository;

	@Before
	public void setUp() throws Exception {
		roadblockRepository = Mockito.mock(RoadblockRepository.class);
		testee = new RoadblockServiceImpl(roadblockRepository);
	}

	@Test
	public void testGetWithinBounds() throws Exception {
		Coordinate sw = new Coordinate(1.0, 2.0);
		Coordinate ne = new Coordinate(3.0, 4.0);
		Roadblock ret = new Roadblock();

		Mockito.when(roadblockRepository.findWithinBounds(1.0, 2.0, 3.0, 4.0)).thenReturn(Collections.singleton(ret));

		Set<Roadblock> roadblocks = testee.getWithinBounds(sw, ne);
		Assert.assertEquals(1, roadblocks.size());
		Assert.assertTrue(roadblocks.contains(ret));
	}
}
