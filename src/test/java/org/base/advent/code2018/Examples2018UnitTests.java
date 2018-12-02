package org.base.advent.code2018;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.util.Point;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
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
}
