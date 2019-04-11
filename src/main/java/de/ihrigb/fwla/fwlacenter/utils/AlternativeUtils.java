package de.ihrigb.fwla.fwlacenter.utils;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class AlternativeUtils {

	@SafeVarargs
	public static <T> Optional<T> alternativeOptional(Supplier<Optional<T>>... suppliers) {
		return AlternativeUtils.alternative(opt -> opt.isPresent(), Optional.empty(), suppliers);
	}

	@SafeVarargs
	public static <T> T alternativeNull(Supplier<T>... suppliers) {
		return AlternativeUtils.alternative(Objects::nonNull, null, suppliers);
	}

	@SafeVarargs
	public static <T> T alternative(Predicate<T> check, T defaultValue, Supplier<T>... suppliers) {
		for (Supplier<T> supplier : suppliers) {
			T t = supplier.get();
			if (check.test(t)) {
				return t;
			}
		}
		return defaultValue;
	}

	private AlternativeUtils() {
	}
}
