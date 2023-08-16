package org.base.advent.code2022;

import org.base.advent.util.Point;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    @Test
    public void testDay08Examples() {
        final List<String> testData = Arrays.asList(
                "30373",
                "25512",
                "65332",
                "33549",
                "35390");
        final Day08 day08 = new Day08();
        day08.setInput(testData);
        assertEquals(21, day08.solvePart1());
        assertEquals(8, day08.solvePart2());
    }

    @Test
    public void testDay17Examples() {
        final String winds = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>";
        final Day17 example = new Day17();
        example.setInput(winds);
        Day17.Cavern cavern = example.newCavern();
        String str = IntStream.range(0, winds.length() * 2)
                .mapToObj(it -> String.valueOf(cavern.nextWind())).collect(Collectors.joining());
        assertEquals(winds + winds, str);
        cavern.drop(Day17.RockBuilder.Horizontal.apply(Point.of(2, 3)));
        cavern.display();

        final Day17 day17 = new Day17();
        day17.setInput(winds);
        assertEquals(3068L, day17.solvePart1());
        assertEquals(1514285714288L, day17.solvePart2());
    }
}
