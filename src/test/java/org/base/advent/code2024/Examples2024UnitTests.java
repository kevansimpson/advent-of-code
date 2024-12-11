package org.base.advent.code2024;

import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.PuzzleTester;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Unit tests for 2024 daily puzzle examples.
 */
public class Examples2024UnitTests extends PuzzleTester {
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
        testSolutions(new Day04(), testData,
                18, Pair::getLeft,
                9, Pair::getRight);
    }

    @Test
    public void testDay05Examples() {
        final List<String> testData = Arrays.asList(
                "47|53", "97|13", "97|61", "97|47", "75|29", "61|13", "75|53", "29|13", "97|29", "53|29", "61|53",
                "97|53", "61|29", "47|13", "75|47", "97|75", "47|61", "75|61", "47|29", "75|13", "53|13",
                "",
                "75,47,61,53,29", "97,61,53,29,13", "75,29,13", "75,97,47,61,53", "61,13,29", "97,13,75,29,47");
        testSolutions(new Day05(), testData,
                143, Pair::getLeft,
                123, Pair::getRight);
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
        testSolutions(new Day06(), testData,
                41, Pair::getLeft,
                6, Pair::getRight);
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
        testSolutions(new Day07(), testData,
                3749L, Pair::getLeft,
                11387L, Pair::getRight);
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
        testSolutions(new Day08(), testData,
                14L, Pair::getLeft,
                34, Pair::getRight);
    }

    @Test
    public void testDay09Examples() {
        final String testData = "2333133121414131402";
        testSolutions(new Day09(pool), testData,
                1928L, Pair::getLeft,
                2858L, Pair::getRight);
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
        testSolutions(new Day10(pool), testData,
                36, Pair::getLeft,
                81, Pair::getRight);
    }
}
