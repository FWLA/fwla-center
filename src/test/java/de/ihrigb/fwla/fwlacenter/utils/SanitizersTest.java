package de.ihrigb.fwla.fwlacenter.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import de.ihrigb.fwla.fwlacenter.api.Address;
import de.ihrigb.fwla.fwlacenter.api.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class SanitizersTest {

	@Test
	public void testClearWhitespaceOnlySanitizedFields() throws Exception {
		TestBean bean = new TestBean("", " ");
		Sanitizers.clearWhitespaceOnlyFields(bean);
		assertNull(bean.getA());
		assertNull(bean.getB());
	}

	@Test
	public void testClearWhitespaceOnlyFields() throws Exception {
		TestBean bean = new TestBean("value", null);
		Sanitizers.clearWhitespaceOnlyFields(bean);
		assertEquals("value", bean.getA());
		assertNull(bean.getB());
	}

	@Test
	public void testClearWhitespaceOnlyExcludedFields() throws Exception {
		TestBean bean = new TestBean("", " ");
		Sanitizers.clearWhitespaceOnlyFields(bean, "b");
		assertNull(bean.getA());
		assertEquals(" ", bean.getB());
	}

	@Test
	public void testStringSanitizer() throws Exception {
		assertNull(Sanitizers.STRING_SANITIZER.apply(null));
		assertEquals("", Sanitizers.STRING_SANITIZER.apply(""));
		assertEquals("", Sanitizers.STRING_SANITIZER.apply(" "));
		assertEquals("", Sanitizers.STRING_SANITIZER.apply("  "));
		assertEquals("a", Sanitizers.STRING_SANITIZER.apply("a"));
		assertEquals("a", Sanitizers.STRING_SANITIZER.apply(" a "));
		assertEquals("a", Sanitizers.STRING_SANITIZER.apply("  a  "));
		assertEquals("ab", Sanitizers.STRING_SANITIZER.apply("ab"));
		assertEquals("a b", Sanitizers.STRING_SANITIZER.apply("a b"));
		assertEquals("a b", Sanitizers.STRING_SANITIZER.apply("a  b"));
		assertEquals("a b", Sanitizers.STRING_SANITIZER.apply("a   b"));
	}

	@Test
	public void testStreetSanitizer() throws Exception {
		assertNull(Sanitizers.STREET_SANITIZER.apply(null));
		assertEquals("", Sanitizers.STREET_SANITIZER.apply(""));
		assertEquals("Musterstraße", Sanitizers.STREET_SANITIZER.apply("Musterstraße"));
		assertEquals("Muster-Straße", Sanitizers.STREET_SANITIZER.apply("Muster-Straße"));
		assertEquals("Muster-Straße", Sanitizers.STREET_SANITIZER.apply("Muster-Str."));
		assertEquals("Musterstraße", Sanitizers.STREET_SANITIZER.apply("Musterstr."));
		assertEquals("Musterstraße", Sanitizers.STREET_SANITIZER.apply("Musterstrasse"));
		assertEquals("Muster-Straße", Sanitizers.STREET_SANITIZER.apply("Muster-Strasse"));
	}

	@Test
	public void testAddressSanitizer() throws Exception {
		Sanitizers.ADDRESS_SANITIZER.accept(null);

		Address address = new Address();
		address.setStreet("Musterstr.");

		Sanitizers.ADDRESS_SANITIZER.accept(address);

		assertEquals("Musterstraße", address.getStreet());
	}

	@Test
	public void testLocationSanitizer() throws Exception {
		Sanitizers.LOCATION_SANITIZER.accept(null);

		Address address = new Address();
		address.setStreet("Musterstr.");

		Location location = new Location();
		location.setAddress(address);

		Sanitizers.LOCATION_SANITIZER.accept(location);

		assertEquals("Musterstraße", location.getAddress().getStreet());
	}

	@Getter
	@AllArgsConstructor
	public static class TestBean {
		private String a;
		private String b;
	}
}
