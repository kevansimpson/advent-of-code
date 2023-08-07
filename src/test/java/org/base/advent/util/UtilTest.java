package org.base.advent.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.base.advent.util.Util.columns;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilTest {
    @Test
    public void testColumns() {
        List<String> rows = Arrays.asList(
                "147",
                "258",
                "369");
        List<String> cols = columns(rows);
        assertEquals(Arrays.asList("123", "456", "789"), cols);
    }
}
