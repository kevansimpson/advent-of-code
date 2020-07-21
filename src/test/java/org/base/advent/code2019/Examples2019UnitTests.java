package org.base.advent.code2019;

import org.base.advent.code2019.intCode.Program;
import org.base.advent.util.Point;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

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

    @Test
    public void testDay06Examples() {
        final Day06 day06 = new Day06();
        final List<String> input = Arrays.asList("COM)B", "B)C", "C)D", "D)E", "E)F", "B)G", "G)H", "D)I", "E)J", "J)K", "K)L");
        assertEquals(42, day06.totalOrbits(input));
        // part 2
        final List<String> input2 = Arrays.asList(
                "COM)B", "B)C", "C)D", "D)E", "E)F", "B)G", "G)H", "D)I", "E)J", "J)K", "K)L", "K)YOU", "I)SAN");
        assertEquals(4, day06.minimalTransfers("YOU", "SAN", input2));
    }

//    @Test
    public void testDay07Examples() {
        final Day07 day07 = new Day07();
        final int[] codes1a = new int[] { 3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0 };
        assertEquals(43210, day07.calcThrust(false, new int [] {4,3,2,1,0}, codes1a));
        final int[] codes1b = new int[] { 3,23,3,24,1002,24,10,24,1002,23,-1,23,101,5,23,23,1,24,23,23,4,23,99,0,0 };
        assertEquals(54321, day07.calcThrust(false, new int [] {0,1,2,3,4}, codes1b));
        final int[] codes1c = new int[] {
                3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33,1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0 };
        assertEquals(65210, day07.calcThrust(false, new int [] {1,0,4,3,2}, codes1c));
        // part 2
        final int[] codes2a = new int[] {
                3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5 };
        assertEquals(139629729, day07.calcThrust(true, new int [] {9,8,7,6,5}, codes2a));
        final int[] codes2b = new int[] {
                3,52,1001,52,-5,52,3,53,1,52,56,54,1007,54,5,55,1005,55,26,1001,54,-5,54,1105,1,12,1,53,54,53,
                1008,54,0,55,1001,55,1,55,2,53,55,53,4,53,1001,56,-1,56,1005,56,6,99,0,0,0,0,10 };
        assertEquals(18216, day07.calcThrust(true, new int [] {9,7,8,5,6}, codes2b));
    }

    @Test
    public void testDay08Examples() {
        final Day08 day08 = new Day08();
        assertEquals("0110", day08.drawImage("0222112222120000", 2, 2));
    }

    @Test
    public void testDay10Examples() {
        final Day10 day10 = new Day10();
        assertEquals(8, day10.maxAsteroids(".#..#", ".....", "#####", "....#", "...##"));
        assertEquals(33, day10.maxAsteroids(
                "......#.#.", "#..#.#....", "..#######.", ".#.#.###..", ".#..#.....",
                "..#....#.#", "#..#....#.", ".##.#..###", "##...#..#.", ".#....####"));
        assertEquals(35, day10.maxAsteroids(
                "#.#...#.#.", ".###....#.", ".#....#...", "##.#.#.#.#", "....#.#.#.",
                ".##..###.#", "..#...##..", "..##....##", "......#...", ".####.###."));
        assertEquals(41, day10.maxAsteroids(
                ".#..#..###", "####.###.#", "....###.#.", "..###.##.#", "##.##.#.#.",
                "....###..#", "..#.#..#.#", "#..#.#.###", ".##...##.#", ".....#.#.."));
        final String[] input = {
                ".#..##.###...#######", "##.############..##.", ".#.######.########.#", ".###.#######.####.#.",
                "#####.##.#.##.###.##", "..#####..#.#########", "####################", "#.####....###.#.#.##",
                "##.#################", "#####.##.###..####..", "..######..##.#######", "####.##.####...##..#",
                ".#####..#.######.###", "##...#.##########...", "#.##########.#######", ".####.#.###.###.#.##",
                "....##.##.###..#####", ".#.#.###########.###", "#.#.#.#####.####.###", "###.##.####.##.#..##"};
        assertEquals(210, day10.maxAsteroids(input));
        // part 2
//        System.out.println(Point.of(11, 13).angle(Point.of(0, 12)));
//
//        System.out.println(Point.of(11, 13).angle(Point.of(10, 13)));
//        System.out.println(Point.of(11, 13).angle(Point.of(12, 13)));
//        System.out.println(Point.of(11, 13).angle(Point.of(11, 14)));
//        System.out.println(Point.of(11, 13).angle(Point.of(11, 12)));
//        System.out.println(Point.of(11, 13).angle(Point.of(12, 1)));
//        System.out.println(Point.of(11, 13).angle(Point.of(12, 2)));
//        System.out.println(Point.of(11, 13).angle(Point.of(12, 8)));

        assertEquals(Point.of(8,2), day10.vaporize(input));
    }

}
