package org.base.advent.code2025;

import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.PuzzleTester;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Unit tests for 2025 daily puzzle examples.
 */
public class Examples2025UnitTests extends PuzzleTester {
    private final ExecutorService pool = Executors.newSingleThreadExecutor();

    @Test
    public void testDay01Examples() {
        final List<String> testData = Arrays.asList(
                "L68",
                "L30",
                "R48",
                "L5",
                "R60",
                "L55",
                "L1",
                "L99",
                "R14",
                "L82");
        testSolutions(new Day01(), testData,
                3, Pair::getLeft,
                6, Pair::getRight);
    }
}
