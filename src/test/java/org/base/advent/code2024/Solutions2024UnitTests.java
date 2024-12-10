package org.base.advent.code2024;

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
                2057374, Day01.DiffScore::diff,
                23177084, Day01.DiffScore::score);
    }

    @Test
    public void verifyDay02() {
        testSolutions(new Day02(), readLines("/2024/input02.txt"),
                534, Day02.Levels::safe,
                577, Day02.Levels::singleBad);
    }

    @Test
    public void verifyDay03() {
        testSolutions(new Day03(), readInput("/2024/input03.txt"),
                184122457, Day03.MulProducts::all,
                107862689, Day03.MulProducts::enabled);
    }

    @Test
    public void verifyDay04() {
        testSolutions(new Day04(), readLines("/2024/input04.txt"),
                2530, Day04.Xmas::count,
                1921, Day04.Xmas::cross);
    }

    @Test
    public void verifyDay05() {
        testSolutions(new Day05(), readLines("/2024/input05.txt"),
                6612, Day05.SafetyManual::middleSum,
                4944, Day05.SafetyManual::corrected);
    }

    @Test
    public void verifyDay06() {
        testSolutions(new Day06(), readLines("/2024/input06.txt"),
                4454, Day06.GuardSteps::unique,
                1503, Day06.GuardSteps::loops);
    }

    @Test
    public void verifyDay07() {
        testSolutions(new Day07(), readLines("/2024/input07.txt"),
                42283209483350L, Day07.RopeBridge::calibration,
                1026766857276279L, Day07.RopeBridge::concatenation);
    }

    @Test
    public void verifyDay08() {
        testSolutions(new Day08(), readLines("/2024/input08.txt"),
                327L, Day08.Antinodes::count,
                1233, Day08.Antinodes::harmonics);
    }

    @Test
    public void verifyDay09() {
        testSolutions(new Day09(pool), readInput("/2024/input09.txt"),
                6334655979668L, Day09.FileSystem::checksum1,
                6349492251099L, Day09.FileSystem::checksum2);
    }

    @Test
    public void verifyDay10() {
        testSolutions(new Day10(pool), readLines("/2024/input10.txt"),
                733, Day10.Trailheads::sum,
                1514, Day10.Trailheads::rating);
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
