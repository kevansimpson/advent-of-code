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

    @Test
    public void testDay05Examples() {
        final List<String> testData = Arrays.asList(
                "    [D]    ",
                "[N] [C]    ",
                "[Z] [M] [P]",
                " 1   2   3",
                "",
                "move 1 from 2 to 1",
                "move 3 from 1 to 3",
                "move 2 from 2 to 1",
                "move 1 from 1 to 2");
        final Day05 day05 = new Day05();
        day05.setInput(testData);
        assertEquals("CMZ", day05.solvePart1());
        assertEquals("MCD", day05.solvePart2());
    }

}
