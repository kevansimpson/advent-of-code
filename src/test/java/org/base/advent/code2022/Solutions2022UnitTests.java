package org.base.advent.code2022;

import org.base.advent.PuzzleTester;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Verify answers for AoC days in 2022 (originally done in Kotlin).
 */
public class Solutions2022UnitTests extends PuzzleTester {
    @Test
    public void verifyDay01() {
        testSolutions(new Day01(), 67633, 199628);
    }

    @Test
    public void verifyDay02() {
        testSolutions(new Day02(), 13005, 11373);
    }

    @Test
    public void verifyDay03() {
        testSolutions(new Day03(), 8088, 2522);
    }

    @BeforeAll
    public static void start() {
        banner(2022);
    }

    @AfterAll
    public static void stop() {
        banner(2022);
    }
}
