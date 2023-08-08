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

    @Test
    public void verifyDay04() {
        testSolutions(new Day04(), 498L, 859L);
    }

    @Test
    public void verifyDay05() {
        testSolutions(new Day05(), "VRWBSFZWM", "RBTWJWMCF");
    }

    @Test
    public void verifyDay06() {
        testSolutions(new Day06(), 1275, 3605);
    }

    @Test
    public void verifyDay07() {
        testSolutions(new Day07(), 1334506, 7421137);
    }

    @Test
    public void verifyDay08() {
        testSolutions(new Day08(), 1779, 172224);
    }

    @Test
    public void verifyDay09() {
        testSolutions(new Day09(), 5981, 2352);
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
