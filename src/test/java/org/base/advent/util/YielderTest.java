package org.base.advent.util;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Yielder}
 */
@SuppressWarnings("BusyWait")
public class YielderTest {
    @SuppressWarnings("ForLoopReplaceableByForEach")
    @Test
    public void testBasicFlow() throws Exception {
        final Yielder<Integer> yint = new Yielder<>() {
            @SneakyThrows
            @Override
            public void run() {
                for (final Integer foo : List.of(1, 2, 3)) this.yield(foo);
            }
        };

        int expected = 0;
        for (final Integer foo : yint) {
            assertEquals(++expected, foo);
            Thread.sleep(25);
        }
        expected = 0;
        for (final Iterator<Integer> foo = yint.iterator(); foo.hasNext();) {
            assertEquals(++expected, foo.next());
            Thread.sleep(25);
        }
    }
}
