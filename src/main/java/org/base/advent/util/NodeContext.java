package org.base.advent.util;

import java.util.List;

/**
 * Defines contract for recursive context for a {@link Node}.
 * @param <T> The generic type for the recursion's primary data.
 */
public interface NodeContext<T> {
    /**
     * Returns the next bits of data with which to recurse.
     * @return the next bits of data with which to recurse.
     */
    List<T> getNext();

    NodeContext<T> newContext(final T next);
}
