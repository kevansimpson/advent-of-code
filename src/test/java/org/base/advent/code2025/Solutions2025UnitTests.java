package org.base.advent.code2025;

import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.PuzzleTester;
import org.base.advent.code2024.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Verify answers for AoC days in 2025.
 */
public class Solutions2025UnitTests extends PuzzleTester {
    private static final ExecutorService pool = Executors.newFixedThreadPool(20);


    @Test
    public void verifyDay01() {
        testSolutions(new Day01(), readLines("/2025/input01.txt"),
                1177, Pair::getLeft,
                6768, Pair::getRight);
    }

    @BeforeAll
    public static void start() {
        banner(2025);
    }

    @AfterAll
    public static void stop() {
        banner(2025);
    }
}
