package org.base.advent;

import org.apache.commons.lang3.BooleanUtils;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;


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


    default String readableTime(Duration duration) {
        long nanos = duration.toNanos();
        if (nanos > 1000000L)
            return String.format("%d millis", TimeUnit.NANOSECONDS.toMillis(nanos));
        else if (nanos > 1000L)
            return String.format("%d micros", TimeUnit.NANOSECONDS.toMicros(nanos));
        else
            return String.format("%d nanos", nanos);
    }

}
