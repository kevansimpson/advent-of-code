package org.base.advent.code2016;

import org.base.advent.PuzzleTester;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Verify answers for AoC days in 2016 (originally done in Ruby and lost).
 */
public class Solutions2016UnitTests extends PuzzleTester {
    @Test
    public void verifyDay01() {
        testSolutions(new Day01(), readInput("/2016/input01.txt"),
                288L, Day01.EasterBunnyHQ::distance,
                111L, Day01.EasterBunnyHQ::alreadyVisited);
    }

    @Test
    public void verifyDay02() {
        testSolutions(new Day02(), readLines("/2016/input02.txt"), "76792", "A7AC3");
    }

    @Test
    public void verifyDay03() {
        testSolutions(new Day03(), readLines("/2016/input03.txt"), 982L, 1826L);
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
