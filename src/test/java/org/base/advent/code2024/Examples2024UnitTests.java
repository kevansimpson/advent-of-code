package org.base.advent.code2024;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for 2024 daily puzzle examples.
 */
public class Examples2024UnitTests {
    private final ExecutorService pool = Executors.newSingleThreadExecutor();

    @Test
    public void testDay04Examples() {
        final List<String> testData = Arrays.asList(
                "MMMSXXMASM",
                "MSAMXMSMSA",
                "AMXSXMAAMM",
                "MSAMASMSMX",
                "XMASAMXAMM",
                "XXAMMXXAMA",
                "SMSMSASXSS",
                "SAXAMASAAA",
                "MAMMMXMMMM",
                "MXMXAXMASX");
        final Day04 day04 = new Day04();
        Day04.Xmas xmas = day04.apply(testData);
        assertEquals(18, xmas.count());
        assertEquals(9, xmas.cross());
    }

    @Test
    public void testDay05Examples() {
        final List<String> testData = Arrays.asList(
                "47|53", "97|13", "97|61", "97|47", "75|29", "61|13", "75|53", "29|13", "97|29", "53|29", "61|53",
                "97|53", "61|29", "47|13", "75|47", "97|75", "47|61", "75|61", "47|29", "75|13", "53|13",
                "",
                "75,47,61,53,29", "97,61,53,29,13", "75,29,13", "75,97,47,61,53", "61,13,29", "97,13,75,29,47");
        final Day05 day05 = new Day05();
        Day05.SafetyManual xmas = day05.apply(testData);
        assertEquals(143, xmas.middleSum());
        assertEquals(123, xmas.corrected());
    }

    @Test
    public void testDay06Examples() {
        final List<String> testData = Arrays.asList(
                "....#.....",
                ".........#",
                "..........",
                "..#.......",
                ".......#..",
                "..........",
                ".#..^.....",
                "........#.",
                "#.........",
                "......#...");
        final Day06 day06 = new Day06();
        Day06.GuardSteps path = day06.apply(testData);
        assertEquals(41, path.unique());
        assertEquals(6, path.loops());
    }

    @Test
    public void testDay07Examples() {
        final List<String> testData = Arrays.asList(
                "190: 10 19",
                "3267: 81 40 27",
                "83: 17 5",
                "156: 15 6",
                "7290: 6 8 6 15",
                "161011: 16 10 13",
                "192: 17 8 14",
                "21037: 9 7 18 13",
                "292: 11 6 16 20");
        final Day07 day07 = new Day07();
        Day07.RopeBridge path = day07.apply(testData);
        assertEquals(3749, path.calibration());
        assertEquals(11387, path.concatenation());
    }

    @Test
    public void testDay08Examples() {
        final List<String> testData = Arrays.asList(
                "............",
                "........0...",
                ".....0......",
                ".......0....",
                "....0.......",
                "......A.....",
                "............",
                "............",
                "........A...",
                ".........A..",
                "............",
                "............");
        final Day08 day08 = new Day08();
        Day08.Antinodes nodes = day08.apply(testData);
        assertEquals(14L, nodes.count());
        assertEquals(34, nodes.harmonics());
    }

    @Test
    public void testDay09Examples() {
        final String testData = "2333133121414131402";
        final Day09 day09 = new Day09();
        Day09.FileSystem nodes = day09.apply(testData);
        assertEquals(1928L, nodes.checksum1());
        assertEquals(2858L, nodes.checksum2());
    }

    @Test
    public void testDay10Examples() {
        final List<String> testData = Arrays.asList(
                "89010123",
                "78121874",
                "87430965",
                "96549874",
                "45678903",
                "32019012",
                "01329801",
                "10456732");
        final Day10 day10 = new Day10(pool);
        Day10.Trailheads trailheads = day10.apply(testData);
        assertEquals(36, trailheads.sum());
        assertEquals(81, trailheads.rating());
    }
}
