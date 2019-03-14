package de.ihrigb.fwla.fwlacenter.utils;

import org.springframework.util.Assert;

public final class ArrayUtils {

	public static <T> boolean contains(T[] array, T needle) {
		return ArrayUtils.indexOf(array, needle) >= 0;
	}

	public static <T> int indexOf(T[] array, T needle) {
		Assert.notNull(needle, "Needle must not be null.");

		int index = -1;
		if (array == null || array.length == 0) {
			return index;
		}

		for (int i = 0; i < array.length; i++) {
			if (needle.equals(array[i])) {
				index = i;
				break;
			}
		}

		return index;
	}

	private ArrayUtils() {
	}
}
