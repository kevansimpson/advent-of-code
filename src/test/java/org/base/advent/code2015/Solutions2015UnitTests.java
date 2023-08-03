package org.base.advent.code2015;

import org.base.advent.PuzzleTester;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Verify answers for days of 2015 Advent of Code.
 */
public class Solutions2015UnitTests extends PuzzleTester {
    @Test
    public void verifyDay01() {
        testSolutions(new Day01(), 74, 1795);
    }

    @Test
    public void verifyDay02() {
        testSolutions(new Day02(), 1588178, 3783758);
    }

    @Test
    public void verifyDay03() {
        testSolutions(new Day03(), 2081, 2341);
    }

    @Test
    public void verifyDay04() {
        testSolutions(new Day04(), 254575L, 1038736L);
    }

    @Test
    public void verifyDay05() {
        testSolutions(new Day05(), 258, 53);
    }

    @Test
    public void verifyDay06() {
        testSolutions(new Day06(), 543903, 14687245);
    }

    @Test
    public void verifyDay07() {
        testSolutions(new Day07(), 46065, 14134);
    }

    @Test
    public void verifyDay08() {
        testSolutions(new Day08(), 1333, 2046);
    }

    @Test
    public void verifyDay09() {
        testSolutions(new Day09(), 207, 804);
    }

    @Test
    public void verifyDay10() {
        testSolutions(new Day10(), 492982, 6989950);
    }

    @Test
    public void verifyDay11() {
        testSolutions(new Day11(), "vzbxxyzz", "vzcaabcc");
    }

    @Test
    public void verifyDay12() {
        testSolutions(new Day12(), 111754, 65402);
    }

    @Test
    public void verifyDay13() {
        testSolutions(new Day13(), 733, 725);
    }
    
    @Test
    public void verifyDay14() {
        testSolutions(new Day14(), 2696, 1084);
    }

    @Test
    public void verifyDay15() {
        testSolutions(new Day15(), 18965440, 15862900);
    }

    @Test
    public void verifyDay16() {
        testSolutions(new Day16(), 40, 241);
    }

    @Test
    public void verifyDay17() {
        testSolutions(new Day17(), 1304, 18);
    }

    @Test
    public void verifyDay18() {
        testSolutions(new Day18(), 821, 886);
    }

    @Test
    public void verifyDay19() {
        testSolutions(new Day19(), 509, 195);
    }

    @Test
    public void verifyDay20() {
        testSolutions(new Day20(), 786240, 831600);
    }

    @Test
    public void verifyDay21() {
        testSolutions(new Day21(), 111, 188);
    }

    @Test
    public void verifyDay22() {
        testSolutions(new Day22(), 1824, 1937);
    }

    @Test
    public void verifyDay23() {
        testSolutions(new Day23(), 307, 160);
    }

    @Test
    public void verifyDay24() {
        testSolutions(new Day24(), 11846773891L, 80393059L); // part 1 = 20s
    }

    @Test
    public void verifyDay25() {
        testSolutions(new Day25(), 13431666L, 2650453L);
    }

    @BeforeAll
    public static void start() {
        banner(2015);
    }

    @AfterAll
    public static void stop() {
        banner(2015);
    }

}
