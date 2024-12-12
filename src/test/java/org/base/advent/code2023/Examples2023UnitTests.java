package org.base.advent.code2023;

import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.PuzzleTester;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Unit tests for 2023 daily puzzle examples.
 */
public class Examples2023UnitTests extends PuzzleTester {
    private final ExecutorService pool = Executors.newSingleThreadExecutor();

    @Test
    public void testDay03Examples() {
        final List<String> testData = Arrays.asList(
                "467..114..",
                "...*......",
                "..35..633.",
                "......#...",
                "617*......",
                ".....+.58.",
                "..592.....",
                "......755.",
                "...$.*....",
                ".664.598..");
        testSolutions(new Day03(), testData,
                4361, Pair::getLeft,
                467835, Pair::getRight);
    }
}
