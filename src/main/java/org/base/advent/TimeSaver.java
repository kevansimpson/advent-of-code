package org.base.advent;

import org.apache.commons.lang3.BooleanUtils;

import java.util.function.Supplier;

/**
 * Reads puzzles.
 */
public interface TimeSaver  {
    default boolean isFullSolve() {
        return BooleanUtils.toBoolean(System.getProperty("full"));
    }
    default void enableFullSolve() {
        System.setProperty("full", "true");
    }

    default <T> T fastOrFull(T fast, Supplier<T> full) {
        if (isFullSolve())
            return full.get();
        else
            return fast;
    }

    default <T> T fullOrFast(T full, T fast) {
        if (isFullSolve())
            return full;
        else
            return fast;
    }
}
