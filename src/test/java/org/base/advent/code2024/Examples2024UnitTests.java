package org.base.advent.code2024;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for 2024 daily puzzle examples.
 */
public class Examples2024UnitTests {
    @Test
    public void testDay04Examples() {
        final List<String> testData = Arrays.asList(
                "MMMSXXMASM",
                "MSAMXMSMSA",
                "AMXSXMAAMM",
                "MSAMASMSMX",
                "XMASAMXAMM",
                "XXAMMXXAMA",
                "SMSMSASXSS",
                "SAXAMASAAA",
                "MAMMMXMMMM",
                "MXMXAXMASX");
        final Day04 day04 = new Day04();
        Day04.Xmas xmas = day04.apply(testData);
        assertEquals(18, xmas.count());
        assertEquals(9, xmas.cross());
    }
}
