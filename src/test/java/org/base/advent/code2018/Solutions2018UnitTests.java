package org.base.advent.code2018;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Verify answers for all days.
 */
public class Solutions2018UnitTests {
    @Test
    public void verifyDay01() throws Exception {
        final Day01 day01 = new Day01();
        assertEquals(518, day01.solvePart1());
        assertEquals(72889, day01.solvePart2());
    }

    @Test
    public void verifyDay02() throws Exception {
        final Day02 day02 = new Day02();
        assertEquals(3952, day02.solvePart1());
        assertEquals("vtnikorkulbfejvyznqgdxpaw", day02.solvePart2());
    }

    @Test
    public void verifyDay03() throws Exception {
        final Day03 day03 = new Day03();
        assertEquals(124850L, day03.solvePart1());
        assertEquals(1097, day03.solvePart2());
    }

    @Test
    public void verifyDay04() throws Exception {
        final Day04 day04 = new Day04();
        assertEquals(19025, day04.solvePart1());
        assertEquals(23776, day04.solvePart2());
    }

    @Test
    public void verifyDay05() throws Exception {
        final Day05 day05 = new Day05();
        assertEquals(10584, day05.solvePart1());
        assertEquals(6968, day05.solvePart2());
    }

    @Test
    public void verifyDay06() throws Exception {
        final Day06 day06 = new Day06();
        assertEquals(3840, day06.solvePart1());
        assertEquals(46542, day06.solvePart2());
    }

//    @Test
//    public void verifyDay07() throws Exception {
//        final Day07 day07 = new Day07();
//        assertEquals("hmvwl", day07.solvePart1());
//        assertEquals(1853, day07.solvePart2());
//    }
//
//    @Test
//    public void verifyDay08() throws Exception {
//        final Day08 day08 = new Day08();
//        assertEquals(5075, day08.solvePart1());
//        assertEquals(7310, day08.solvePart2());
//    }
//
//    @Test
//    public void verifyDay09() throws Exception {
//        final Day09 day09 = new Day09();
//        assertEquals(16869, day09.solvePart1());
//        assertEquals(7284, day09.solvePart2());
//    }
//
//    @Test
//    public void verifyDay10() throws Exception {
//        final Day10 day10 = new Day10();
//        assertEquals(4480, day10.solvePart1());
//        assertEquals("c500ffe015c83b60fad2e4b7d59dabc4", day10.solvePart2());
//    }
//
//    @Test
//    public void verifyDay11() throws Exception {
//        final Day11 day11 = new Day11();
//        assertEquals(643, day11.solvePart1());
//        assertEquals(1471, day11.solvePart2());
//    }
//
//    @Test
//    public void verifyDay12() throws Exception {
//        final Day12 day12 = new Day12();
//        assertEquals(152, day12.solvePart1());
//        assertEquals(186, day12.solvePart2());
//    }
//
//    @Test
//    public void verifyDay13() throws Exception {
//        final Day13 day13 = new Day13();
//        assertEquals(1504, day13.solvePart1());
//        assertEquals(3823370, day13.solvePart2());
//    }
//
//    @Test
//    public void verifyDay14() throws Exception {
//        final Day14 day14 = new Day14();
//        assertEquals(8194, day14.solvePart1());
//        assertEquals(1141, day14.solvePart2());
//    }
//
//    @Test
//    public void verifyDay15() throws Exception {
//        final Day15 day15 = new Day15();
//        if (!day15.cache()) {
//            assertEquals(650, day15.solvePart1());
//            assertEquals(336, day15.solvePart2());
//        }
//    }
//
//    @Test
//    public void verifyDay16() throws Exception {
//        final Day16 day16 = new Day16();
//        assertEquals("namdgkbhifpceloj", day16.solvePart1());
//        assertEquals("ibmchklnofjpdeag", day16.solvePart2());
//    }
//
//    @Test
//    public void verifyDay17() throws Exception {
//        final Day17 day17 = new Day17();
//        assertEquals(600, day17.solvePart1());
//        assertEquals(31220910, day17.solvePart2());
//    }
//
//    @Test
//    public void verifyDay18() throws Exception {
//        final Day18 day18 = new Day18();
//        assertEquals(7071L, day18.solvePart1());
//        assertEquals(8001, day18.solvePart2());
//    }
//
//    @Test
//    public void verifyDay19() throws Exception {
//        final Day19 day19 = new Day19();
//        assertEquals("QPRYCIOLU", day19.solvePart1());
//        assertEquals(16162, day19.solvePart2());
//    }
//
//    @Test
//    public void verifyDay20() throws Exception {
//        final Day20 day20 = new Day20();
//        assertEquals(258, day20.solvePart1());
//        assertEquals(707, day20.solvePart2());
//    }

}
