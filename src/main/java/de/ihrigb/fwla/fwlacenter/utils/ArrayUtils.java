package de.ihrigb.fwla.fwlacenter.utils;

public final class ArrayUtils {

	public static <T> boolean contains(T[] array, T needle) {
		return ArrayUtils.indexOf(array, needle) >= 0;
	}

	public static <T> int indexOf(T[] array, T needle) {
		int index = -1;
		if (array == null || array.length == 0) {
			return index;
		}

		for (int i = 0; i < array.length; i++) {
			T atIndex = array[i];
			if ((needle == null && atIndex == null) || (needle != null && needle.equals(atIndex))) {
				index = i;
				break;
			}
		}

		return index;
	}

	private ArrayUtils() {
	}
}
