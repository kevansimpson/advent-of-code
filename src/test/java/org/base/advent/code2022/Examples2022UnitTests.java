package org.base.advent.code2022;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for 2022 daily puzzle examples.
 */
public class Examples2022UnitTests {
    @Test
    public void testDay03Examples() {
        final List<String> testData = Arrays.asList(
                "vJrwpWtwJgWrhcsFMMfFFhFp",
                "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL",
                "PmmdzqPrVvPwwTWBwg",
                "wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn",
                "ttgJtRGJQctTZtZT",
                "CrZsJsPPZsGzwwsLwLmpwMDw");
        final Day03 day03 = new Day03();
        assertEquals(157, day03.sumPriorities(testData));
        assertEquals(70, day03.sumThreeElves(testData));
    }
}
