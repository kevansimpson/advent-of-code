package org.base.advent.code2019;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for daily puzzle examples.
 */
public class Examples2019UnitTests {

    @Test
    public void testDay01Examples() {
        final Day01 day01 = new Day01();
        // For a mass of 12, divide by 3 and round down to get 4, then subtract 2 to get 2.
        assertEquals(2, day01.calculate(12));
        // For a mass of 14, dividing by 3 and rounding down still yields 4, so the fuel required is also 2.
        assertEquals(2, day01.calculate(14));
        // For a mass of 1969, the fuel required is 654.
        assertEquals(654, day01.calculate(1969));
        // For a mass of 100756, the fuel required is 33583.
        assertEquals(33583, day01.calculate(100756));

        // part 2
        assertEquals(2, day01.accumulate(14));
        assertEquals(966, day01.accumulate(1969));
        assertEquals(50346, day01.accumulate(100756));
    }

    @Test
    public void testDay02Examples() {
        final Day02 day02 = new Day02();
        assertArrayEquals(new int[] { 3500,9,10,70,2,3,11,0,99,30,40,50 },
                day02.runProgram(1,9,10,3,2,3,11,0,99,30,40,50));
        assertArrayEquals(new int[] { 2,0,0,0,99 }, day02.runProgram(1,0,0,0,99));
        assertArrayEquals(new int[] { 2,3,0,6,99 }, day02.runProgram(2,3,0,3,99));
        assertArrayEquals(new int[] { 2,4,4,5,99,9801 }, day02.runProgram(2,4,4,5,99,0));
        assertArrayEquals(new int[] { 30,1,1,4,2,5,6,0,99 }, day02.runProgram(1,1,1,4,99,5,6,0,99));
    }

    @Test
    public void testDay03Examples() {
        final Day03 day03 = new Day03();
        assertEquals(6, day03.closestIntersection(split("R8,U5,L5,D3", "U7,R6,D4,L4")));
        assertEquals(159, day03.closestIntersection(split(
                "R75,D30,R83,U83,L12,D49,R71,U7,L72", "U62,R66,U55,R34,D71,R55,D58,R83")));
        assertEquals(135, day03.closestIntersection(split(
                "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51", "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7")));
        // part 2
        assertEquals(30, day03.fewestSteps(split("R8,U5,L5,D3", "U7,R6,D4,L4")));
        assertEquals(610, day03.fewestSteps(split(
                "R75,D30,R83,U83,L12,D49,R71,U7,L72", "U62,R66,U55,R34,D71,R55,D58,R83")));
        assertEquals(410, day03.fewestSteps(split(
                "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51", "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7")));
//        R75,D30,R83,U83,L12,D49,R71,U7,L72
//        U62,R66,U55,R34,D71,R55,D58,R83 = 610 steps
//                R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51
//        U98,R91,D20,R16,D67,R40,U7,R15,U6,R7 = 410 steps
    }

    private List<String[]> split(final String... input) {
        return Stream.of(input).map(str -> str.split(",")).collect(Collectors.toList());
    }
    /*
    @Test
    public void testDay04Examples() throws Exception {
        final Day04 day04 = new Day04();
        final List<String> input = Arrays.asList(
                "[1518-11-01 00:00] Guard #10 begins shift",
                "[1518-11-01 00:05] falls asleep",
                "[1518-11-01 00:25] wakes up",
                "[1518-11-01 00:30] falls asleep",
                "[1518-11-01 00:55] wakes up",
                "[1518-11-01 23:58] Guard #99 begins shift",
                "[1518-11-02 00:40] falls asleep",
                "[1518-11-02 00:50] wakes up",
                "[1518-11-03 00:05] Guard #10 begins shift",
                "[1518-11-03 00:24] falls asleep",
                "[1518-11-03 00:29] wakes up",
                "[1518-11-04 00:02] Guard #99 begins shift",
                "[1518-11-04 00:36] falls asleep",
                "[1518-11-04 00:46] wakes up",
                "[1518-11-05 00:03] Guard #99 begins shift",
                "[1518-11-05 00:45] falls asleep",
                "[1518-11-05 00:55] wakes up");
        final Day04.Guard guard = day04.findSleepiestGuard(input);
        assertEquals(10, guard.getId());
        assertEquals(24, guard.getSleepiestMinute());
        assertEquals(240, day04.strategy1(input));
        assertEquals(4455, day04.strategy2(input));
    }

    @Test
    public void testDay05Examples() throws Exception {
        final Day05 day05 = new Day05();
        assertEquals("", day05.formPolymer("aA"));
        assertEquals("", day05.formPolymer("abBA"));
        assertEquals("abAB", day05.formPolymer("abAB"));
        assertEquals("aabAAB", day05.formPolymer("aabAAB"));
        assertEquals("dabCBAcaDA", day05.formPolymer("dabAcCaCBAcCcaDA"));
        // part 2
        assertEquals("daDA", day05.improvePolymer("dabAcCaCBAcCcaDA"));
    }

    @Test
    public void testDay06Examples() throws Exception {
        final Day06 day06 = new Day06();
        final List<String> input = Arrays.asList("1, 1", "1, 6", "8, 3", "3, 4", "5, 5", "8, 9");
        final List<Point> points = day06.toPoints(input);
        assertEquals(17, day06.findLargestArea(points));
        // part 2
        assertEquals(16, day06.findSafestArea(points, 32));
    }
     */
}