package org.base.advent.code2024;

import org.base.advent.PuzzleTester;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Verify answers for AoC days in 2024.
 */

public class Solutions2024UnitTests extends PuzzleTester {
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

    @BeforeAll
    public static void start() {
        banner(2024);
    }

    @AfterAll
    public static void stop() {
        banner(2024);
    }
}
