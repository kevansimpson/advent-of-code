package org.base.advent.code2019;

import org.base.advent.Solution;
import org.base.advent.code2019.intCode.Channel;

import static org.base.advent.code2019.intCode.Program.boostProgram;

/**
 * <a href="https://adventofcode.com/2019/day/05">Day 05</a>
 */
public class Day05 implements Solution<long[]> {
    @Override
    public Object solvePart1(final long[] input) {
        return waitForDiagnostic(1, input);
    }

    @Override
    public Object solvePart2(final long[] input) {
        return waitForDiagnostic(5, input);
    }

    public static long waitForDiagnostic(long input, final long... codes) {
        Channel output = boostProgram(codes, input);
        return output.stream().filter(it -> it != 0).findFirst().orElseThrow();
    }
}
