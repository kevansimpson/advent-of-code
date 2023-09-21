package org.base.advent.code2019;

import org.base.advent.Solution;

import java.util.Deque;

import static org.base.advent.code2019.intCode.Program.runProgram;
import static org.base.advent.code2019.intCode.Program.simpleChannel;

/**
 * <a href="https://adventofcode.com/2019/day/05">Day 05</a>
 */
public class Day05 implements Solution<int[]> {
    @Override
    public Object solvePart1(final int[] input) {
        return waitForDiagnostic(1, input);
    }

    @Override
    public Object solvePart2(final int[] input) {
        return waitForDiagnostic(5, input);
    }

    public static int waitForDiagnostic(int input, final int... codes) {
        Deque<Integer> output = simpleChannel();
        runProgram(codes, simpleChannel(input), output);
        return output.stream().filter(it -> it != 0).findFirst().orElseThrow();
    }
}
