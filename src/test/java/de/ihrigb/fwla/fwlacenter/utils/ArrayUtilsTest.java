package de.ihrigb.fwla.fwlacenter.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ArrayUtilsTest {

	@Test
	public void testContainsTrue() throws Exception {
		assertTrue("ArrayUtils.contains() should return true.",
				ArrayUtils.contains(new String[] { "value1", "value2" }, "value2"));
	}

	@Test
	public void testContainsFalse() throws Exception {
		assertFalse("ArrayUtils.contains() should return false.",
				ArrayUtils.contains(new String[] { "value1", "value2" }, "value3"));
	}

	@Test
	public void testIndexOfArrayNull() throws Exception {
		assertEquals("indexOf should return -1", -1, ArrayUtils.indexOf(null, null));
	}

	@Test
	public void testIndexOfEmptyArray() throws Exception {
		assertEquals("indexOf should return -1", -1, ArrayUtils.indexOf(new String[0], null));
	}

	@Test
	public void testIndexOfNullNeedleMissing() throws Exception {
		assertEquals("indexOf should return -1", -1, ArrayUtils.indexOf(new String[] { "value" }, null));
	}

	@Test
	public void testIndexOfNullNeedleFound1() throws Exception {
		assertEquals("indexOf should return 0", 0, ArrayUtils.indexOf(new String[] { null, "value" }, null));
	}

	@Test
	public void testIndexOfNullNeedleFound2() throws Exception {
		assertEquals("indexOf should return 1", 1, ArrayUtils.indexOf(new String[] { "value", null }, null));
	}

	@Test
	public void testIndexOfNullNeedleNotFound() throws Exception {
		assertEquals("indexOf should return 1", 1, ArrayUtils.indexOf(new String[] { "value1", "value2" }, "value3"));
	}
}
