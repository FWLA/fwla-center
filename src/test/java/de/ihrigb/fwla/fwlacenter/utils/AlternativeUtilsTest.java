package de.ihrigb.fwla.fwlacenter.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.util.Objects;
import java.util.Optional;

import org.junit.Test;

public class AlternativeUtilsTest {

	@Test
	public void testAlternativeOptionalNoSuppliers() throws Exception {
		assertFalse("No suppliers should return empty optional.", AlternativeUtils.alternativeOptional().isPresent());
	}

	@Test
	public void testAlternativeOptionalFirstSupplier() throws Exception {
		assertEquals("Single supplier does return value.", Optional.of("value"),
				AlternativeUtils.alternativeOptional(() -> Optional.of("value")));
	}

	@Test
	public void testAlternativeOptionalFirstSupplierEmpty() throws Exception {
		assertEquals("Second supplier does return value.", Optional.of("value2"),
				AlternativeUtils.alternativeOptional(() -> Optional.empty(), () -> Optional.of("value2")));
	}

	@Test
	public void testAlternativeNullNoSuppliers() throws Exception {
		assertNull("No suppliers should return null.", AlternativeUtils.alternativeNull());
	}

	@Test
	public void testAlternativeNullFirstSupplier() throws Exception {
		assertEquals("Single supplier does return value.", "value", AlternativeUtils.alternativeNull(() -> "value"));
	}

	@Test
	public void testAlternativeNullFirstSupplierEmpty() throws Exception {
		assertEquals("Second supplier does return value.", "value2",
				AlternativeUtils.alternativeNull(() -> null, () -> "value2"));
	}

	@Test
	public void testAlternativeNoSuppliers() throws Exception {
		assertNull("No suppliers, default value null must return null.",
				AlternativeUtils.alternative(Objects::nonNull, null));
	}

	@Test
	public void testAlternativeFirstSupplierDefaultValue() throws Exception {
		assertEquals("First supplier does return value.", "default",
				AlternativeUtils.alternative(Objects::nonNull, "default"));
	}

	@Test
	public void testAlternativeFirstSupplier() throws Exception {
		assertEquals("First supplier does return value.", "value",
				AlternativeUtils.alternative(Objects::nonNull, null, () -> "value"));
	}

	@Test
	public void testAlternativeFirstSupplierNull() throws Exception {
		assertEquals("Second supplier does return value.", "value2",
				AlternativeUtils.alternative(Objects::nonNull, null, () -> null, () -> "value2"));
	}
}
