package org.base.advent.code2024;

import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.PuzzleTester;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Verify answers for AoC days in 2024.
 */
public class Solutions2024UnitTests extends PuzzleTester {
    private static final ExecutorService pool = Executors.newFixedThreadPool(20);


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

    @Test
    public void verifyDay13() {
        testSolutions(new Day13(pool), readLines("/2024/input13.txt"),
                33209L, Pair::getLeft,
                83102355665474L, Pair::getRight);
    }

    @Test
    public void verifyDay14() {
        testSolutions(new Day14(101, 103), readLines("/2024/input14.txt"),
                208437768, Pair::getLeft,
                7492, Pair::getRight);
    }

    @Test
    public void verifyDay15() {
        testParallelSolutions(new Day15(pool), readLines("/2024/input15.txt"),
                1457740, 1467145);
    }

    @Test
    public void verifyDay16() {
        testSolutions(new Day16(), readLines("/2024/input16.txt"),
                85432, Pair::getLeft,
                465, Pair::getRight);
    }

    @Test
    public void verifyDay17() {
        testParallelSolutions(new Day17(pool), readLines("/2024/input17.txt"),
                "3,5,0,1,5,1,5,1,0", 107413700225434L);
    }

    @Test
    public void verifyDay18() {
        testSolutions(new Day18(71, 71, 1024), readLines("/2024/input18.txt"),
                454L, Pair::getLeft,
                "8,51", Pair::getRight);
    }

    @Test
    public void verifyDay19() {
        testSolutions(new Day19(), readLines("/2024/input19.txt"),
                251, Pair::getLeft,
                616957151871345L, Pair::getRight);
    }

    @Test
    public void verifyDay20() {
        testSolutions(new Day20(100), readLines("/2024/input20.txt"),
                1454, Pair::getLeft,
                997879, Pair::getRight);
    }

    @Test
    public void verifyDay21() {
        testSolutions(new Day21(), Arrays.asList("935A", "319A", "480A", "789A", "176A"),
                188398L, Pair::getLeft,
                230049027535970L, Pair::getRight);
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
