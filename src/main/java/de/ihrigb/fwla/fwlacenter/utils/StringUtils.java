package de.ihrigb.fwla.fwlacenter.utils;

import java.util.Collection;

import org.springframework.util.Assert;

public class StringUtils {

	public static boolean containsIgnoreCase(Collection<String> col, String needle) {
		Assert.notNull(needle, "Search string must not be null");

		if (col == null) {
			return false;
		}

		for (String str : col) {
			if (needle.equalsIgnoreCase(str)) {
				return true;
			}
		}
		return false;
	}
}
