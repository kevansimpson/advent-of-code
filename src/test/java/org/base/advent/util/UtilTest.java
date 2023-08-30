package org.base.advent.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class UtilTest {
    @Test
    public void testExtractLong() {
        String str = "foo" + Long.MAX_VALUE +"bar1234567890";
        assertArrayEquals(new long[] { Long.MAX_VALUE, 1234567890L }, Text.extractLong(str));
    }
}
