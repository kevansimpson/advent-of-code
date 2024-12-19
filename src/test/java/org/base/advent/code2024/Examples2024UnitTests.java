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

    @Test
    public void testDay11Examples() {
        final String testData = "125 17";
        testSolutions(new Day11(), testData,
                55312L, Pair::getLeft,
                65601038650482L, Pair::getRight);
    }

    @Test
    public void testDay12Examples() {
        final List<String> testData = Arrays.asList(
                "RRRRIICCFF",
                "RRRRIICCCF",
                "VVRRRCCFFF",
                "VVRCCCJFFF",
                "VVVVCJJCFE",
                "VVIVCCJJEE",
                "VVIIICJJEE",
                "MIIIIIJJEE",
                "MIIISIJEEE",
                "MMMISSJEEE");
        Day12 day12 = new Day12();
        testSolutions(day12, List.of("AAAA", "BBCD", "BBCC", "EEEC"),
                140, Pair::getLeft, 80, Pair::getRight);
        testSolutions(day12, List.of("OOOOO", "OXOXO", "OOOOO", "OXOXO", "OOOOO"),
                772, Pair::getLeft, 436, Pair::getRight);
        testSolutions(day12, testData, 1930, Pair::getLeft, 1206, Pair::getRight);
    }

    @Test
    public void testDay13Examples() {
        final List<String> testData = Arrays.asList(
                "Button A: X+94, Y+34",
                "Button B: X+22, Y+67",
                "Prize: X=8400, Y=5400",
                "",
                "Button A: X+26, Y+66",
                "Button B: X+67, Y+21",
                "Prize: X=12748, Y=12176",
                "",
                "Button A: X+17, Y+86",
                "Button B: X+84, Y+37",
                "Prize: X=7870, Y=6450",
                "",
                "Button A: X+69, Y+23",
                "Button B: X+27, Y+71",
                "Prize: X=18641, Y=10279");
        testSolutions(new Day13(pool), testData, 480L, Pair::getLeft, 875318608908L, Pair::getRight);
    }

    @Test
    public void testDay14Examples() {
        final List<String> testData = Arrays.asList(
                "p=0,4 v=3,-3",
                "p=6,3 v=-1,-3",
                "p=10,3 v=-1,2",
                "p=2,0 v=2,-1",
                "p=0,0 v=1,3",
                "p=3,0 v=-2,-2",
                "p=7,6 v=-1,-3",
                "p=3,0 v=-1,-2",
                "p=9,3 v=2,3",
                "p=7,3 v=-1,2",
                "p=2,4 v=2,-3",
                "p=9,5 v=-3,-3");
        testSolutions(new Day14(11, 7), testData,
                12, Pair::getLeft,
                0, Pair::getRight);
    }

    @Test
    public void testDay15Examples() {
        final List<String> vertical = Arrays.asList(
                "#######",
                "#...#.#",
                "#.....#",
                "#..OO@#",
                "#..O..#",
                "#.....#",
                "#######",
                "",
                "<vv<<^^<<^^");
        testParallelSolutions(new Day15(pool), vertical, 908, 618);

        final List<String> testData = Arrays.asList(
                "##########",
                "#..O..O.O#",
                "#......O.#",
                "#.OO..O.O#",
                "#..O@..O.#",
                "#O#..O...#",
                "#O..O..O.#",
                "#.OO.O.OO#",
                "#....O...#",
                "##########",
                "",
                "<vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^",
                "vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v",
                "><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<",
                "<<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^",
                "^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><",
                "^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^",
                ">^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^",
                "<><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>",
                "^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>",
                "v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^");

//        testParallelSolutions(new Day15(pool), testData, 10092, 9021);
    }

}
