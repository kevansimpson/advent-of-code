package org.base.advent.util;

import java.util.List;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility methods.
 */
public class Util {
    public static <T> int sum(final List<T> list, final ToIntFunction<T> function) {
        return list.stream().mapToInt(function).sum();
    }

    public static List<String[]> split(final String... input) {
        return Stream.of(input).map(str -> str.split(",")).collect(Collectors.toList());
    }

}
