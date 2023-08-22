package org.base.advent.code2019;

import org.base.advent.Solution;
import org.base.advent.code2019.intCode.Program;

/**
 * <a href="https://adventofcode.com/2019/day/05">Day 05</a>
 */
public class Day05 implements Solution<int[]> {
    @Override
    public Object solvePart1(final int[] input) {
        return Program.runProgram(() -> 1, input).getOutput();
    }

    @Override
    public Object solvePart2(final int[] input) {
        return Program.runProgram(() -> 5, input).getOutput();
    }
}
