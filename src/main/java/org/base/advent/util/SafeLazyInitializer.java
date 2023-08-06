package org.base.advent.util;

import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.LazyInitializer;

import java.util.function.Supplier;

/**
 * Extension of {@link LazyInitializer} to remove exceptions from method signatures.
 * @param <T> The type returned by the initializer.
 */
public class SafeLazyInitializer<T> extends LazyInitializer<T> {
    private final Supplier<T> initializer;

    public SafeLazyInitializer(Supplier<T> supplier) {
        initializer = supplier;
    }

    @Override
    public T get() {
        try {
            return super.get();
        }
        catch (ConcurrentException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected T initialize() {
        return initializer.get();
    }
}
