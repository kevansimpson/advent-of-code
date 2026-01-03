package org.base.advent.code2025;

import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.PuzzleTester;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Unit tests for 2025 daily puzzle examples.
 */
public class Examples2025UnitTests extends PuzzleTester {
    private final ExecutorService pool = Executors.newSingleThreadExecutor();

    @Test
    public void testDay01Examples() {
        final List<String> testData = Arrays.asList(
                "L68",
                "L30",
                "R48",
                "L5",
                "R60",
                "L55",
                "L1",
                "L99",
                "R14",
                "L82");
        testSolutions(new Day01(), testData, 3, Pair::getLeft, 6, Pair::getRight);
    }

    @Test
    public void testDay02Examples() {
        final String testData =
                "11-22,95-115,998-1012,1188511880-1188511890,222220-222224,"
                + "1698522-1698528,446443-446449,38593856-38593862,565653-565659,"
                + "824824821-824824827,2121212118-2121212124";
        testSolutions(new Day02(), testData,
                1227775554L, Pair::getLeft,
                4174379265L, Pair::getRight);
    }

    @Test
    public void testDay03Examples() {
        final List<String> testData = Arrays.asList(
                "987654321111111",
                "811111111111119",
                "234234234234278",
                "818181911112111");
        testSolutions(new Day03(), testData,
                357L, Pair::getLeft,
                3121910778619L, Pair::getRight);
    }

    @Test
    public void testDay04Examples() {
        final List<String> testData = Arrays.asList(
                "..@@.@@@@.",
                "@@@.@.@.@@",
                "@@@@@.@.@@",
                "@.@@@@..@.",
                "@@.@@@@.@@",
                ".@@@@@@@.@",
                ".@.@.@.@@@",
                "@.@@@.@@@@",
                ".@@@@@@@@.",
                "@.@.@@@.@.");
        testSolutions(new Day04(), testData,
                13, Pair::getLeft,
                43, Pair::getRight);
    }

    @Test
    public void testDay05Examples() {
        final List<String> testData = Arrays.asList(
                "3-5", "10-14", "16-20", "12-18",
                "",
                "1", "5", "8", "11", "17", "32");
        testSolutions(new Day05(), testData,
                3, Pair::getLeft,
                14L, Pair::getRight);
    }

    @Test
    public void testDay06Examples() {
        final List<String> testData = Arrays.asList(
                "123 328  51 64 ",
                " 45 64  387 23 ",
                "  6 98  215 314",
                "*   +   *   +  ");
        testParallelSolutions(new Day06(pool), testData, 4277556L, 3263827L);
    }

    @Test
    public void testDay07Examples() {
        final List<String> testData = Arrays.asList(
                ".......S.......",
                "...............",
                ".......^.......",
                "...............",
                "......^.^......",
                "...............",
                ".....^.^.^.....",
                "...............",
                "....^.^...^....",
                "...............",
                "...^.^...^.^...",
                "...............",
                "..^...^.....^..",
                "...............",
                ".^.^.^.^.^...^.",
                "...............");
        testParallelSolutions(new Day07(pool), testData, 21, 40L);
    }

    @Test
    public void testDay08Examples() {
        final List<String> testData = Arrays.asList(
                "162,817,812", "57,618,57", "906,360,560", "592,479,940", "352,342,300",
                "466,668,158", "542,29,236", "431,825,988", "739,650,466", "52,470,668",
                "216,146,977", "819,987,18", "117,168,530", "805,96,715", "346,949,466",
                "970,615,88", "941,993,340", "862,61,35", "984,92,344", "425,690,689");
        testSolutions(new Day08(10), testData,
                40, Pair::getLeft,
                25272L, Pair::getRight);
    }

    @Test
    public void testDay09Examples() {
        final List<String> testData = Arrays.asList(
                "7,1", "11,1", "11,7", "9,7",
                "9,5", "2,5", "2,3", "7,3");
        testSolutions(new Day09(2), testData,
                50L, Pair::getLeft,
                24L, Pair::getRight);
    }

    @Test
    public void testDay10Examples() {
        final List<String> testData = Arrays.asList(
                "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}",
                "[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}",
                "[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}");
        testSolutions(new Day10(pool), testData,
                7, Pair::getLeft,
                33L, Pair::getRight);
    }

    @Test
    public void testDay11Examples() {
        final List<String> testData1 = Arrays.asList(
                "aaa: you hhh",
                "you: bbb ccc",
                "bbb: ddd eee",
                "ccc: ddd eee fff",
                "ddd: ggg",
                "eee: out",
                "fff: out",
                "ggg: out",
                "hhh: ccc fff iii",
                "iii: out");
        testSolutions(new Day11(pool), testData1, 5, Pair::getLeft, 0L, Pair::getRight);
        final List<String> testData2 = Arrays.asList(
                "svr: aaa bbb",
                "aaa: fft",
                "fft: ccc",
                "bbb: tty",
                "tty: ccc",
                "ccc: ddd eee",
                "ddd: hub",
                "hub: fff",
                "eee: dac",
                "dac: fff",
                "fff: ggg hhh",
                "ggg: out",
                "hhh: out");
        testSolutions(new Day11(pool), testData2, 0, Pair::getLeft, 2L, Pair::getRight);
    }
}
