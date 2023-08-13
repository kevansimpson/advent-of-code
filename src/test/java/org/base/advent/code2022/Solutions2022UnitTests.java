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

    @Test
    public void verifyDay10() {
        testSolutions(new Day10(), 15260, "PGHFGLUG");
    }

    @Test
    public void verifyDay11() {
        testSolutions(new Day11(), 58056L, 15048718170L);
    }

    @Test
    public void verifyDay12() {
        testSolutions(new Day12(), 383L, 377L);
    }

    @Test
    public void verifyDay13() {
        testSolutions(new Day13(), 5555, 22852);
    }

    @Test
    public void verifyDay14() {
        testSolutions(new Day14(), 737L, 28145L);
    }

    @Test
    public void verifyDay15() {
        testSolutions(new Day15(), 5335787L, 13673971349056L);
    }

    @Test
    public void verifyDay16() {
        System.setProperty("full", "true");
        testSolutions(new Day16(), 1923, 2594);
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
