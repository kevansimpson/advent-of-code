package org.base.advent;

import org.apache.commons.lang3.BooleanUtils;

import java.util.Arrays;


/**
 * Identifies two part solutions from an Advent of Code problem.
 */
public interface Helpers {
    /** Sorts characters in specified string. */
    default String sortString(final String input) {
        final char[] tempArray = input.toCharArray();
        Arrays.sort(tempArray);
        return new String(tempArray);
    }

    default boolean cache() {
        return BooleanUtils.toBoolean(System.getProperty("aoc.cache", "true"));
    }

    default boolean debug() {
        return BooleanUtils.toBoolean(System.getProperty("aoc.debug", "false"));
    }

    default void debug(final String message, final Object... args) {
        if (debug())
            //noinspection UseOfSystemOutOrSystemErr
            System.out.printf(message + "%n", args);
    }
}
