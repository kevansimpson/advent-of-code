package org.base.advent.code2016;

import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.PuzzleTester;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Unit tests for 2016 daily puzzle examples.
 */
public class Examples2016UnitTests extends PuzzleTester {
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
        assertFalse(new Day03().validTriangle(5, 10, 25));
    }

    @Test
    public void testDay17Examples() {
        testSolutions(new Day17(), "ihgpwlah",
                "DDRRRD", Pair::getLeft, 370, Pair::getRight);
        testSolutions(new Day17(), "kglvqrro",
                "DDUDRLRRUDRD", Pair::getLeft, 492, Pair::getRight);
        testSolutions(new Day17(), "ulqzkmiv",
                "DRURDRUDDLLDLUURRDULRLDUUDDDRR", Pair::getLeft, 830, Pair::getRight);
    }
}
