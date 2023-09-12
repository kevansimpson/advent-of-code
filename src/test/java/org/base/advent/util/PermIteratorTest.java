package org.base.advent.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link PermIterator}
 */
public class PermIteratorTest {
    @Test
    public void testIterator() {
        verifyIterator("a", "b");
        verifyIterator("a", "b", "c");
    }

    @SafeVarargs
    final <T> void verifyIterator(final T... items) {
        int count = 0, expected = Util.factorial(items.length).intValue();
        final PermIterator<T> permIterator = new PermIterator<>(items);
        for (final List<T> perm : permIterator) {
            assertNotNull(perm);
            System.out.println("test => "+ perm);
            assertEquals(items.length, perm.size());
            ++count;
        }
        assertEquals(expected, count);
    }
}
