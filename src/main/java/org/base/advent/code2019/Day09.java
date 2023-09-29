package org.base.advent.code2019;

import org.base.advent.Solution;

import static org.base.advent.code2019.intCode.Program.boostProgram;

/**
 * <a href="https://adventofcode.com/2019/day/9">Day 9</a>
 */
public class Day09 implements Solution<long[]> {
    @Override
    public Object solvePart1(long[] input) {
        return boostProgram(input, 1L).peek();
    }

    @Override
    public Object solvePart2(long[] input) {
        return boostProgram(input, 2L).peek();
    }
}