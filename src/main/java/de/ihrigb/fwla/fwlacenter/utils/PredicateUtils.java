package de.ihrigb.fwla.fwlacenter.utils;

import java.util.function.Predicate;

public final class PredicateUtils {

    public static <T> Predicate<T> not(Predicate<T> p) {
        return p.negate();
    }
}
