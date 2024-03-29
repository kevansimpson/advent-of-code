package org.base.advent.code2018;

import org.base.advent.PuzzleTester;
import org.base.advent.util.Point;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for daily puzzle examples.
 */
public class Examples2018UnitTests extends PuzzleTester {

    @Test
    public void testDay01Examples() {
        final Day01 day01 = new Day01();
        assertEquals(3, day01.sum(Arrays.asList(1, 1, 1)));
        assertEquals(0, day01.sum(Arrays.asList(1, 1, -2)));
        assertEquals(-6, day01.sum(Arrays.asList(-1, -2, -3)));
        // part 2
        assertEquals(0, day01.findDuplicateFrequency(Arrays.asList(1, -1)));
        assertEquals(10, day01.findDuplicateFrequency(Arrays.asList(3, 3, 4, -2, -4)));
        assertEquals(5, day01.findDuplicateFrequency(Arrays.asList(-6, 3, 8, 5, -6)));
        assertEquals(14, day01.findDuplicateFrequency(Arrays.asList(7, 7, -2, -7, -4)));
    }

    @Test
    public void testDay02Examples() {
        final Day02 day02 = new Day02();
        assertEquals(Day02.Match.none, day02.countPairsAndTriples("abcdef"));
        assertEquals(Day02.Match.both, day02.countPairsAndTriples("bababc"));
        assertEquals(Day02.Match.two, day02.countPairsAndTriples("abbcde"));
        assertEquals(Day02.Match.three, day02.countPairsAndTriples("abcccd"));
        assertEquals(Day02.Match.two, day02.countPairsAndTriples("aabcdd"));
        assertEquals(Day02.Match.two, day02.countPairsAndTriples("abcdee"));
        assertEquals(Day02.Match.three, day02.countPairsAndTriples("ababab"));
        assertEquals(12, day02.checksum(Arrays.asList(
                "abcdef", "bababc", "abbcde", "abcccd", "aabcdd", "abcdee", "ababab")));
        // part 2
        assertEquals("fgij", day02.findPrototype(Arrays.asList(
                "abcde", "fghij", "klmno", "pqrst", "fguij", "axcye", "wvxyz")));
    }

    @Test
    public void testDay03Examples() {
        final Day03 day03 = new Day03();
        final Day03.Claim claim = day03.toClaim("#123 @ 3,2: 5x4");
        assertEquals(123, claim.getId());
        assertEquals(3, claim.getLeft());
        assertEquals(2, claim.getTop());
        assertEquals(5, claim.getWidth());
        assertEquals(4, claim.getHeight());

        final List<String> input = Arrays.asList("#1 @ 1,3: 4x4", "#2 @ 3,1: 4x4", "#3 @ 5,5: 2x2");
        List<Day03.Claim> claims = day03.toClaims(input);
        final Map<Point, List<Integer>> grid = day03.buildGrid(claims);
        assertEquals(4, day03.calculateOverlap(grid));
        assertEquals(3, day03.findAdjacentClaimId(claims, grid));
    }

    @Test
    public void testDay04Examples() {
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
    public void testDay05Examples() {
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
    public void testDay06Examples() {
        final Day06 day06 = new Day06();
        final List<String> input = Arrays.asList("1, 1", "1, 6", "8, 3", "3, 4", "5, 5", "8, 9");
        final List<Point> points = day06.toPoints(input);
        assertEquals(17, day06.findLargestArea(points));
        // part 2
        assertEquals(16, day06.findSafestArea(points, 32));
    }

    @Test
    public void testDay08Examples() {
        testSolutions(new Day08(), "2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2",
                138L, Day08.LicenseTree::metadataSum,
                66L, Day08.LicenseTree::rootNodeValue);
    }

    @Test
    public void testDay09Examples() {
        final Day09 day09 = new Day09();
        assertEquals(32, day09.playGame(new Day09.MarbleGame(9, 25)));
        assertEquals(8317, day09.playGame(new Day09.MarbleGame(10, 1618)));
        assertEquals(146373, day09.playGame(new Day09.MarbleGame(13, 7999)));
        assertEquals(2764, day09.playGame(new Day09.MarbleGame(17, 1104)));
        assertEquals(54718, day09.playGame(new Day09.MarbleGame(21, 6111)));
        assertEquals(37305, day09.playGame(new Day09.MarbleGame(30, 5807)));
    }
}
