package org.base.advent;

import org.apache.commons.lang3.BooleanUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;


/**
 * Identifies two part solutions from an Advent of Code problem.
 */
public interface Helpers {

    /**
     * Recursively builds all permutations of available list, applying the specified consumer when available list is empty.
     *
     * @param available All available items.
     * @param permutation The current permutation of items under evaluation.
     * @param noneAvailable The consumer to apply when there are no available items.
     * @param <I> The type of item.
     */
    default <I> void buildAllPaths(final List<I> available, final List<I> permutation, final Consumer<List<I>> noneAvailable) {
        if (available.isEmpty()) {
            noneAvailable.accept(permutation);
            return;
        }

        for (int i = 0; i < available.size(); i++) {
            final I loc = available.get(i);
            final List<I> remaining = new ArrayList<>();
            remaining.addAll(available.subList(0, i));
            remaining.addAll(available.subList(i + 1, available.size()));

            final List<I> newPerm = new ArrayList<>(permutation);
            newPerm.add(loc);
            buildAllPaths(remaining, newPerm, noneAvailable);
        }
    }

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
