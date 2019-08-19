package de.ihrigb.fwla.fwlacenter.services.layer;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.ihrigb.fwla.fwlacenter.api.Coordinate;
import de.ihrigb.fwla.fwlacenter.persistence.model.RailwayCoordinateBox;
import de.ihrigb.fwla.fwlacenter.persistence.repository.RailwayCoordinateBoxRepository;
import de.ihrigb.fwla.fwlacenter.services.api.geo.FeatureDetails;
import de.ihrigb.fwla.fwlacenter.services.api.geo.Layer;
import de.ihrigb.fwla.fwlacenter.services.api.geo.LayerGroup;

public class RailwayLayerProviderTest {

	private RailwayLayerProvider testee;

	private RailwayCoordinateBoxRepository repository;

	@Before
	public void setUp() {
		repository = mock(RailwayCoordinateBoxRepository.class);
		testee = new RailwayLayerProvider(repository);
	}

	@Test
	public void testGetLayerGroupsNoCoordinateBoxes() throws Exception {

		expect(repository.count()).andReturn(0L);

		replay(repository);
		assertTrue(testee.getLayerGroups().isEmpty());
		verify(repository);
	}

	@Test
	public void testGetLayerGroups() throws Exception {

		expect(repository.count()).andReturn(1L);

		replay(repository);
		List<LayerGroup> layerGroups = testee.getLayerGroups();
		assertEquals(1, layerGroups.size());
		LayerGroup layerGroup = layerGroups.get(0);
		assertEquals(RailwayLayerProvider.LAYER_GROUP_NAME, layerGroup.getName());
		List<Layer> layers = layerGroup.getLayers();
		assertEquals(1, layers.size());
		Layer layer = layers.get(0);
		assertEquals(RailwayLayerProvider.LAYER_ID, layer.getId());
		assertEquals(RailwayLayerProvider.LAYER_NAME, layer.getName());
		verify(repository);
	}

	@Test
	public void testGetFeaturesWrongLayerId() throws Exception {
		replay(repository);
		assertTrue(testee.getFeatures("wrong").getFeatures().isEmpty());
		verify(repository);
	}

	@Test
	public void testGetFeaturesNoCoordinateBoxes() throws Exception {

		expect(repository.findAll()).andReturn(Collections.emptyList());

		replay(repository);
		assertTrue(testee.getFeatures(RailwayLayerProvider.LAYER_ID).getFeatures().isEmpty());
		verify(repository);
	}

	@Test
	public void testGetFeatures() throws Exception {

		expectRailwayCoordinateBox();

		replay(repository);
		assertEquals(41, testee.getFeatures(RailwayLayerProvider.LAYER_ID).getFeatures().size());
		verify(repository);
	}

	@Test
	public void testGetFeatureDetailsWrongLayerId() throws Exception {

		replay(repository);
		assertFalse(testee.getFeatureDetails("wrong", "wrong").isPresent());
		verify(repository);
	}

	@Test
	public void testGetFeatureDetailsWrongFeatureId() throws Exception {

		expectRailwayCoordinateBox();

		replay(repository);
		assertFalse(testee.getFeatureDetails(RailwayLayerProvider.LAYER_ID, "wrong").isPresent());
		verify(repository);
	}

	@Test
	public void testGetFeatureDetails() throws Exception {

		expectRailwayCoordinateBox();

		replay(repository);
		FeatureDetails featureDetails = testee.getFeatureDetails(RailwayLayerProvider.LAYER_ID, "MPost-452719")
				.orElseGet(() -> {
					fail();
					return null;
				});

		assertEquals("KM 21,00", featureDetails.getName());
		assertEquals("Bahnstrecke Mannheim - Frankfurt Stadion", featureDetails.getText());
		verify(repository);
	}

	private void expectRailwayCoordinateBox() {

		RailwayCoordinateBox railwayCoordinateBox = new RailwayCoordinateBox();
		railwayCoordinateBox.setId("id");
		railwayCoordinateBox.setUpperLeft(new Coordinate(49.666053, 8.362939));
		railwayCoordinateBox.setLowerRight(new Coordinate(49.522720, 8.597398));
		expect(repository.findAll()).andReturn(Collections.singletonList(railwayCoordinateBox));
	}
}
