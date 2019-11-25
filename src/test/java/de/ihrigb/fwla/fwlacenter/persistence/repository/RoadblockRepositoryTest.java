package de.ihrigb.fwla.fwlacenter.persistence.repository;

import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import de.ihrigb.fwla.fwlacenter.api.Coordinate;
import de.ihrigb.fwla.fwlacenter.api.Location;
import de.ihrigb.fwla.fwlacenter.persistence.model.Roadblock;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoadblockRepositoryTest {

	@Autowired
	private RoadblockRepository roadblockRepository;

	private Roadblock r1;
	private Roadblock r2;
	private Roadblock r3;
	private Roadblock r4;

	@Before
	public void setUp() throws Exception {
		r1 = new Roadblock();
		r1.setLocation(new Location());
		r1.getLocation().setCoordinate(new Coordinate(5.0, 5.0));
		r1 = roadblockRepository.save(r1);

		r2 = new Roadblock();
		r2.setLocation(new Location());
		r2.getLocation().setCoordinate(new Coordinate(-5.0, 5.0));
		r2 = roadblockRepository.save(r2);

		r3 = new Roadblock();
		r3.setLocation(new Location());
		r3.getLocation().setCoordinate(new Coordinate(5.0, -5.0));
		r3 = roadblockRepository.save(r3);

		r4 = new Roadblock();
		r4.setLocation(new Location());
		r4.getLocation().setCoordinate(new Coordinate(-5.0, -5.0));
		r4 = roadblockRepository.save(r4);
	}

	@After
	public void tearDown() throws Exception {
		roadblockRepository.deleteAll();
	}

	@Test
	public void testFindWithinBounds() throws Exception {
		callAndExpect(2.0, 2.0, 7.0, 7.0, r1);
		callAndExpect(-7.0, 2.0, -2.0, 7.0, r2);
		callAndExpect(2.0, -7.0, 7.0, -2.0, r3);
		callAndExpect(-7.0, -7.0, -2.0, -2.0, r4);
		callAndExpect(2.0, -7.0, 7.0, 7.0, r1, r3);
		callAndExpect(-7.0, -7.0, -2.0, 7.0, r2, r4);
		callAndExpect(-10.0, -10.0, 10.0, 10.0, r1, r2, r3, r4);
	}

	private void callAndExpect(double swLatitude, double swLongitude, double neLatitude, double neLongitude,
			Roadblock... expectedRoadblocks) {
		Set<Roadblock> roadblocks = roadblockRepository.findWithinBounds(swLatitude, swLongitude, neLatitude,
				neLongitude);
		Assert.assertEquals(expectedRoadblocks.length, roadblocks.size());
		for (Roadblock r : expectedRoadblocks) {
			Assert.assertTrue(roadblocks.contains(r));
		}
	}
}
