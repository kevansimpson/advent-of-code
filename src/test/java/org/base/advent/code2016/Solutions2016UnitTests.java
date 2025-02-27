package org.base.advent.code2016;

import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.PuzzleTester;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Verify answers for AoC days in 2016 (originally done in Ruby and lost).
 */
public class Solutions2016UnitTests extends PuzzleTester {
    private static final ExecutorService pool = Executors.newFixedThreadPool(20);

    @Test
    public void verifyDay01() {
        testSolutions(new Day01(), readInput("/2016/input01.txt"),
                288L, Pair::getLeft, 111L, Pair::getRight);
    }

    @Test
    public void verifyDay02() {
        testSolutions(new Day02(), readLines("/2016/input02.txt"),
                "76792", "A7AC3");
    }

    @Test
    public void verifyDay03() {
        testSolutions(new Day03(), readLines("/2016/input03.txt"),
                982L, 1826L);
    }

    @Test
    public void verifyDay04() {
        testSolutions(new Day04(), readLines("/2016/input04.txt"),
                137896, Pair::getLeft, 501, Pair::getRight);
    }

    @Test
    public void verifyDay05() {
        testSolutions(new Day05(), "uqwqemis",
                "1a3099aa", Pair::getLeft, "694190cd", Pair::getRight);
    }

    @Test
    public void verifyDay06() {
        testSolutions(new Day06(), readLines("/2016/input06.txt"),
                "umcvzsmw", Pair::getLeft, "rwqoacfz", Pair::getRight);
    }

    @Test
    public void verifyDay07() {
        testSolutions(new Day07(), readLines("/2016/input07.txt"),
                118, Pair::getLeft, 260, Pair::getRight);
    }

    @Test
    public void verifyDay08() {
        testSolutions(new Day08(), readLines("/2016/input08.txt"),
                116, "UPOJFLBCEZ");
    }

    @Test
    public void verifyDay09() {
        testSolutions(new Day09(), readInput("/2016/input09.txt"),
                123908L, 10755693147L);
    }

    @Test
    public void verifyDay10() {
        testSolutions(new Day10(), readLines("/2016/input10.txt"),
                116, Pair::getLeft, 23903, Pair::getRight);
    }

    @Test
    public void verifyDay11() {
        testParallelSolutions(new Day11(pool), readLines("/2016/input11.txt"),
                37L, 61L);
    }

    @Test
    public void verifyDay12() {
        testParallelSolutions(new Day12(pool), readLines("/2016/input12.txt"),
                318020, 9227674);
    }

    @Test
    public void verifyDay13() {
        testSolutions(new Day13(), 1362, 82, Pair::getLeft, 138L, Pair::getRight);
    }

    @Test
    public void verifyDay14() {
        testSolutions(new Day14(), "abc", 22728, Pair::getLeft, 22551, Pair::getRight);
        testSolutions(new Day14(), "cuanljph", 23769, Pair::getLeft, 20606, Pair::getRight);
    }

    @Test
    public void verifyDay15() {
        testSolutions(new Day15(), readLines("/2016/input15.txt"),
                121834, Pair::getLeft, 3208099, Pair::getRight);
    }

    @Test
    public void verifyDay16() {
        testSolutions(new Day16(), "11110010111001001",
                "01110011101111011", Pair::getLeft, "11001111011000111", Pair::getRight);
    }

    @Test
    public void verifyDay17() {
        testSolutions(new Day17(), "awrkjxxr",
                "RDURRDDLRD", Pair::getLeft, 526, Pair::getRight);
    }

    @Test
    public void verifyDay18() {
        testSolutions(new Day18(), readInput("/2016/input18.txt"),
                2005, Pair::getLeft, 20008491, Pair::getRight);
    }

    @Test
    public void verifyDay19() {
        testSolutions(new Day19(), 3014603, 1834903, 1420280);
    }

    @Test
    public void verifyDay20() {
        testSolutions(new Day20(), readLines("/2016/input20.txt"),
                17348574L, Pair::getLeft, 104L, Pair::getRight);
    }

    @Test
    public void verifyDay21() {
        testParallelSolutions(new Day21(pool), readLines("/2016/input21.txt"),
                "dgfaehcb", "fdhgacbe");
    }

    @Test
    public void verifyDay22() {
        testParallelSolutions(new Day22(pool), readLines("/2016/input22.txt"),
                1038, 252);
    }

    @Test
    public void verifyDay23() {
        testSolutions(new Day23(), readLines("/2016/input23.txt"),
                11424, Pair::getLeft, 479007984, Pair::getRight);
    }

    @BeforeAll
    public static void start() {
        banner(2016);
    }

    @AfterAll
    public static void stop() {
        banner(2016);
    }
}
