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

    @Test
    public void testDay05Examples() {
        final List<String> testData = Arrays.asList(
                "47|53", "97|13", "97|61", "97|47", "75|29", "61|13", "75|53", "29|13", "97|29", "53|29", "61|53",
                "97|53", "61|29", "47|13", "75|47", "97|75", "47|61", "75|61", "47|29", "75|13", "53|13",
                "",
                "75,47,61,53,29", "97,61,53,29,13", "75,29,13", "75,97,47,61,53", "61,13,29", "97,13,75,29,47");
        final Day05 day05 = new Day05();
        Day05.SafetyManual xmas = day05.apply(testData);
        assertEquals(143, xmas.middleSum());
        assertEquals(123, xmas.corrected());
    }
}
