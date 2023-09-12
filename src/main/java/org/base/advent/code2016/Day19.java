package org.base.advent.code2016;

import org.base.advent.Solution;

import java.util.List;
import java.util.function.Function;

/**
 * <a href="https://adventofcode.com/2016/day/19">Day 19</a>
 */
public class Day19 implements Solution<Integer> {
    @Override
    public Object solvePart1(Integer input) {
        // double the difference b/w count and highest 2^ less than count, add 1
        int pow2 = 4096;
        while (pow2 * 2 < input)
            pow2 *= 2;

        return (input - pow2) * 2 + 1;
    }

    @Override
    public Object solvePart2(Integer input) {
        // difference b/w count and highest 3^ less than count
        int pow3 = 3;
        while (pow3 * 3 < input)
            pow3 *= 3;

        return input - pow3;
    }
}