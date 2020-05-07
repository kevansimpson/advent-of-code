package org.base.advent.util;

import java.util.List;
import java.util.function.ToIntFunction;

/**
 * Utility methods.
 */
public class Util {
    public static <T> int sum(final List<T> list, final ToIntFunction<T> function) {
        return list.stream().mapToInt(function).sum();
    }
}
