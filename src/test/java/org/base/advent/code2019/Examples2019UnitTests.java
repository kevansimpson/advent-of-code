package org.base.advent.code2019;

import org.apache.commons.lang3.ArrayUtils;
import org.base.advent.code2019.intCode.Program;
import org.junit.jupiter.api.Test;

import static org.base.advent.util.Util.split;
import static org.junit.jupiter.api.Assertions.*;

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
        assertArrayEquals(new int[] { 3500,9,10,70,2,3,11,0,99,30,40,50 },
                Program.runProgram(1,9,10,3,2,3,11,0,99,30,40,50));
        assertArrayEquals(new int[] { 2,0,0,0,99 }, Program.runProgram(1,0,0,0,99));
        assertArrayEquals(new int[] { 2,3,0,6,99 }, Program.runProgram(2,3,0,3,99));
        assertArrayEquals(new int[] { 2,4,4,5,99,9801 }, Program.runProgram(2,4,4,5,99,0));
        assertArrayEquals(new int[] { 30,1,1,4,2,5,6,0,99 }, Program.runProgram(1,1,1,4,99,5,6,0,99));
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
    }

    @Test
    public void testDay04Examples() {
        final Day04 day04 = new Day04();
        assertFalse(day04.simple(223450)); // (decreasing pair of digits 50)
        assertFalse(day04.simple(123789)); // (no double)
        assertTrue(day04.simple(111111)); // (double 11, never decreases)

        assertTrue(day04.complex(112233)); // (digits never decrease and all repeated digits are exactly two digits long)
        assertFalse(day04.complex(123444)); // (the repeated 44 is part of a larger group of 444)
        assertTrue(day04.complex(111122)); // (even though 1 is repeated more than twice, it still contains a double 22)
    }

    @Test
    public void testDay05Examples() {
        assertEquals(1138, Program.runProgram(() -> 1138,3,0,4,0,99).getOutput());
        assertEquals(0, Program.runProgram(() -> 1138,1002,4,3,4,33).getOutput());
        // part 2
        final int[] codes = new int[] {
                3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,
                1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,
                999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99 };
        assertEquals(999, Program.runProgram(() -> 1, codes).getOutput());
        assertEquals(1000, Program.runProgram(() -> 8, codes).getOutput());
        assertEquals(1001, Program.runProgram(() -> 1138, codes).getOutput());
    }

    /*
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
