package org.base.advent.code2018;

import org.base.advent.util.Point;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Unit tests for daily puzzle examples.
 */
public class Examples2018UnitTests {

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
    public void testDay03Examples() throws Exception {
        final Day03 day03 = new Day03();
        final Day03.Claim claim = day03.toClaim("#123 @ 3,2: 5x4");
        assertEquals(123, claim.getId());
        assertEquals(3, claim.getLeft());
        assertEquals(2, claim.getTop());
        assertEquals(5, claim.getWidth());
        assertEquals(4, claim.getHeight());

        final List<String> input = Arrays.asList("#1 @ 1,3: 4x4", "#2 @ 3,1: 4x4", "#3 @ 5,5: 2x2");
        day03.setClaims(day03.toClaims(input));
        final Map<Point, List<Integer>> grid = day03.buildGrid(day03.getClaims());
        assertEquals(4, day03.calculateOverlap(grid));
        assertEquals(3, day03.findAdjacentClaimId(grid));
    }

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
}
