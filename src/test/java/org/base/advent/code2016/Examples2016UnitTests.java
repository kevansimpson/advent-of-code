package org.base.advent.code2016;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Unit tests for 2016 daily puzzle examples.
 */
public class Examples2016UnitTests {

    @Test
    public void testDay01Examples() {
        final Day01 day01 = new Day01();
        assertEquals(5, day01.calculateDistance("R2,L3", false));
        assertEquals(2, day01.calculateDistance("R2,R2,R2", false));
        assertEquals(12, day01.calculateDistance("R5,L5,R5,R3", false));
        // part 2
        assertEquals(4, day01.calculateDistance("R8,R4,R4,R8", true));
    }

    @Test
    public void testDay02Examples() {
        final Day02 day02 = new Day02();
        final List<String> input = Arrays.asList("ULL", "RRDDD", "LURDL", "UUUUD");
        assertEquals("1985", day02.followInstructions(input, Day02.squarePad));
        // part 2
        assertEquals("5DB3", day02.followInstructions(input, Day02.diamondPad));
    }

    @Test
    public void testDay03Examples() {
        final Day03 day03 = new Day03();
        assertFalse(day03.validTriangle(5, 10, 25));
    }
}
