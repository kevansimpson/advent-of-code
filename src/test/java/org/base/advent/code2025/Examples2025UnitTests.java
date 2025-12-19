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
}
