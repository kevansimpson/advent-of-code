package org.base.advent.code2025;

import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.PuzzleTester;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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

    @Test
    public void verifyDay02() {
        testSolutions(new Day02(), readInput("/2025/input02.txt"),
                54234399924L, Pair::getLeft,
                70187097315L, Pair::getRight);
    }

    @Test
    public void verifyDay03() {
        testSolutions(new Day03(), readLines("/2025/input03.txt"),
                17100L, Pair::getLeft,
                170418192256861L, Pair::getRight);
    }

    @Test
    public void verifyDay04() {
        testSolutions(new Day04(), readLines("/2025/input04.txt"),
                1351, Pair::getLeft,
                8345, Pair::getRight);
    }

    @Test
    public void verifyDay05() {
        testSolutions(new Day05(), readLines("/2025/input05.txt"),
                865, Pair::getLeft,
                352556672963116L, Pair::getRight);
    }

    @Test
    public void verifyDay06() {
        testParallelSolutions(new Day06(pool), readLines("/2025/input06.txt"),
                6757749566978L, 10603075273949L);
    }

    @Test
    public void verifyDay07() {
        testParallelSolutions(new Day07(pool), readLines("/2025/input07.txt"),
                1553, 15811946526915L);
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
