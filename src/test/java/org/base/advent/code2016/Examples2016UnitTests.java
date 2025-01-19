package org.base.advent.code2016;

import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.PuzzleTester;
import org.base.advent.code2016.Day12.MonorailComputer;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyMap;
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

    @Test
    public void testDay23Examples() {
        MonorailComputer computer = new MonorailComputer(List.of(
                "cpy 2 a", "tgl a", "tgl a", "tgl a", "cpy 1 a", "dec a", "dec a"
        ), emptyMap());
        assertEquals(3, computer.operateAssembunnyCode());
    }
}
