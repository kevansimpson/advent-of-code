package org.base.advent.code2024;

import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.PuzzleTester;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Verify answers for AoC days in 2024.
 */
public class Solutions2024UnitTests extends PuzzleTester {
    private final ExecutorService pool = Executors.newFixedThreadPool(5);


    @Test
    public void verifyDay01() {
        testSolutions(new Day01(), readLines("/2024/input01.txt"),
                2057374, Pair::getLeft,
                23177084, Pair::getRight);
    }

    @Test
    public void verifyDay02() {
        testSolutions(new Day02(), readLines("/2024/input02.txt"),
                534, Pair::getLeft,
                577, Pair::getRight);
    }

    @Test
    public void verifyDay03() {
        testSolutions(new Day03(), readInput("/2024/input03.txt"),
                184122457, Pair::getLeft,
                107862689, Pair::getRight);
    }

    @Test
    public void verifyDay04() {
        testSolutions(new Day04(), readLines("/2024/input04.txt"),
                2530, Pair::getLeft,
                1921, Pair::getRight);
    }

    @Test
    public void verifyDay05() {
        testSolutions(new Day05(), readLines("/2024/input05.txt"),
                6612, Pair::getLeft,
                4944, Pair::getRight);
    }

    @Test
    public void verifyDay06() {
        testSolutions(new Day06(), readLines("/2024/input06.txt"),
                4454, Pair::getLeft,
                1503, Pair::getRight);
    }

    @Test
    public void verifyDay07() {
        testSolutions(new Day07(), readLines("/2024/input07.txt"),
                42283209483350L, Pair::getLeft,
                1026766857276279L, Pair::getRight);
    }

    @Test
    public void verifyDay08() {
        testSolutions(new Day08(), readLines("/2024/input08.txt"),
                327L, Pair::getLeft,
                1233, Pair::getRight);
    }

    @Test
    public void verifyDay09() {
        testSolutions(new Day09(pool), readInput("/2024/input09.txt"),
                6334655979668L, Pair::getLeft,
                6349492251099L, Pair::getRight);
    }

    @Test
    public void verifyDay10() {
        testSolutions(new Day10(pool), readLines("/2024/input10.txt"),
                733, Pair::getLeft,
                1514, Pair::getRight);
    }

    @Test
    public void verifyDay11() {
        testSolutions(new Day11(), readInput("/2024/input11.txt"),
                203609L, Pair::getLeft,
                240954878211138L, Pair::getRight);
    }

    @Test
    public void verifyDay12() {
        testSolutions(new Day12(), readLines("/2024/input12.txt"),
                1304764, Pair::getLeft,
                811148, Pair::getRight);
    }

    @BeforeAll
    public static void start() {
        banner(2024);
    }

    @AfterAll
    public static void stop() {
        banner(2024);
    }
}
