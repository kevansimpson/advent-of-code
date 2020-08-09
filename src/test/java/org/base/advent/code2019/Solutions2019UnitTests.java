package org.base.advent.code2019;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Verify answers for all days.
 */
public class Solutions2019UnitTests {
    @Test
    public void verifyDay01() throws Exception {
        final Day01 day01 = new Day01();
        assertEquals(3266288, day01.solvePart1());
        assertEquals(4896582, day01.solvePart2());
    }

    @Test
    public void verifyDay02() throws Exception {
        final Day02 day02 = new Day02();
        assertEquals(4570637, ((int[]) day02.solvePart1())[0]);
        assertEquals(5485, day02.solvePart2());
    }

    @Test
    public void verifyDay03() throws Exception {
        final Day03 day03 = new Day03();
        assertEquals(352, day03.solvePart1());
        assertEquals(43848, day03.solvePart2());
    }

    @Test
    public void verifyDay04() throws Exception {
        final Day04 day04 = new Day04();
        assertEquals(1178L, day04.solvePart1());
        assertEquals(763L, day04.solvePart2());
    }

    @Test
    public void verifyDay05() throws Exception {
        final Day05 day05 = new Day05();
        assertEquals(13285749, day05.solvePart1().getOutput());
        assertEquals(5000972, day05.solvePart2().getOutput());
    }

    @Test
    public void verifyDay06() throws Exception {
        final Day06 day06 = new Day06();
        assertEquals(249308, day06.solvePart1());
        assertEquals(349, day06.solvePart2());
    }

    @Test
    public void verifyDay07() {
//        final Day07 day07 = new Day07();
//        assertEquals(75228, day07.solvePart1());
//        assertEquals(1853, day07.solvePart2());
    }

    /*
         ##  #### #    #  # #
        #  # #    #    #  # #
        #    ###  #    #  # #
        #    #    #    #  # #
        #  # #    #    #  # #
         ##  #    ####  ##  ####
     */
    @Test
    public void verifyDay08() throws Exception {
        final Day08 day08 = new Day08();
        assertEquals(1935, day08.solvePart1());
        assertEquals(
                "011001111010000100101000010010100001000010010100001000011100100001001010000100001000010000100101000010010100001000010010100000110010000111100110011110",
                day08.solvePart2());
    }

    /*
    @Test
    public void verifyDay09() throws Exception {
        final Day09 day09 = new Day09();
        assertEquals(16869, day09.solvePart1());
        assertEquals(7284, day09.solvePart2());
    }
    */

    @Test
    public void verifyDay10() throws Exception {
        final Day10 day10 = new Day10();
        assertEquals(329, day10.solvePart1());
        assertEquals(512, day10.solvePart2());
    }

    /*
    @Test
    public void verifyDay11() throws Exception {
        final Day11 day11 = new Day11();
        assertEquals(643, day11.solvePart1());
        assertEquals(1471, day11.solvePart2());
    }
    */

    @Test
    public void verifyDay12() throws Exception {
        final Day12 day12 = new Day12();
        assertEquals(9743, day12.solvePart1());
//        assertEquals(288684633706728L, day12.solvePart2()); // 4s
    }

    /*
    @Test
    public void verifyDay13() throws Exception {
        final Day13 day13 = new Day13();
        assertEquals(1504, day13.solvePart1());
        assertEquals(3823370, day13.solvePart2());
    }
    */

    @Test
    public void verifyDay14() throws Exception {
        final Day14 day14 = new Day14();
        assertEquals(857266, day14.solvePart1());
//        assertEquals(2144702, day14.solvePart2()); // 7s
    }

    /*
    @Test
    public void verifyDay15() throws Exception {
        final Day15 day15 = new Day15();
        if (!day15.cache()) {
            assertEquals(650, day15.solvePart1());
            assertEquals(336, day15.solvePart2());
        }
    }
    */

    @Test
    public void verifyDay16() throws Exception {
        final Day16 day16 = new Day16();
        assertEquals("85726502", day16.solvePart1());
        assertEquals("92768399", day16.solvePart2());
    }

    /*
    @Test
    public void verifyDay17() throws Exception {
        final Day17 day17 = new Day17();
        assertEquals(600, day17.solvePart1());
        assertEquals(31220910, day17.solvePart2());
    }
    */

    @Test
    public void verifyDay18() throws Exception {
        final Day18 day18 = new Day18();
        assertEquals(7071L, day18.solvePart1());
//        assertEquals(8001, day18.solvePart2());
    }

    /*
    @Test
    public void verifyDay19() throws Exception {
        final Day19 day19 = new Day19();
        assertEquals("QPRYCIOLU", day19.solvePart1());
        assertEquals(16162, day19.solvePart2());
    }

    @Test
    public void verifyDay20() throws Exception {
        final Day20 day20 = new Day20();
        assertEquals(258, day20.solvePart1());
        assertEquals(707, day20.solvePart2());
    }
     */
}
