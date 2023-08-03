package org.base.advent.code2017;

import org.base.advent.PuzzleTester;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Verify answers for 2017 days.
 */
public class Solutions2017UnitTests extends PuzzleTester {
    @Test
    public void verifyDay01() {
        testSolutions(new Day01(), 1251, 1244);
    }

    @Test
    public void verifyDay02() {
        testSolutions(new Day02(), 41919, 303);
    }

    @Test
    public void verifyDay03() {
        testSolutions(new Day03(), 438, 266330L);
    }

    @Test
    public void verifyDay04() {
        testSolutions(new Day04(), 337L, 231L);
    }

    @Test
    public void verifyDay05() {
        testSolutions(new Day05(), 318883L, 23948711L);
    }

    @Test
    public void verifyDay06() {
        testSolutions(new Day06(), 12841, 8038);
    }

    @Test
    public void verifyDay07() {
        testSolutions(new Day07(), "hmvwl", 1853);
    }

    @Test
    public void verifyDay08() {
        testSolutions(new Day08(), 5075, 7310);
    }

    @Test
    public void verifyDay09() {
        testSolutions(new Day09(), 16869, 7284);
    }

    @Test
    public void verifyDay10() {
        testSolutions(new Day10(), 4480, "c500ffe015c83b60fad2e4b7d59dabc4");
    }

    @Test
    public void verifyDay11() {
        testSolutions(new Day11(), 643, 1471);
    }

    @Test
    public void verifyDay12() {
        testSolutions(new Day12(), 152, 186);
    }

    @Test
    public void verifyDay13() {
        testSolutions(new Day13(), 1504, 3823370);
    }

    @Test
    public void verifyDay14() {
        testSolutions(new Day14(), 8194, 1141);
    }

    @Test
    public void verifyDay15() {
        final Day15 day15 = new Day15();
        if (!day15.cache()) {
            assertEquals(650, day15.solvePart1());
            assertEquals(336, day15.solvePart2());
        }
    }

    @Test
    public void verifyDay16() {
        testSolutions(new Day16(), "namdgkbhifpceloj", "ibmchklnofjpdeag");
    }

    @Test
    public void verifyDay17() {
        testSolutions(new Day17(), 600, 31220910);
    }

    @Test
    public void verifyDay18() {
        testSolutions(new Day18(), 7071L, 8001);
    }

    @Test
    public void verifyDay19() {
        testSolutions(new Day19(), "QPRYCIOLU", 16162);
    }

    @Test
    public void verifyDay20() {
        testSolutions(new Day20(), 258, 707);
    }

    @BeforeAll
    public static void start() {
        banner(2017);
    }

    @AfterAll
    public static void stop() {
        banner(2017);
    }
}
