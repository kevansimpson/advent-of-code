package org.base.advent.code2019;

import org.base.advent.Solution;
import org.base.advent.code2019.intCode.Program;

import java.util.Arrays;

/**
 * <a href="https://adventofcode.com/2019/day/02">Day 02</a>
 */
public class Day02 implements Solution<long[]> {
    private static final int TARGET = 19690720;

    @Override
    public Object solvePart1(final long[] input) {
        // before running the program,
        // replace position 1 with the value 12 and
        // replace position 2 with the value 2.
        return gravityAssist(12, 2, input)[0];
    }

    @Override
    public Object solvePart2(final long[] input) {
        return targetOutput(input);
    }

    int targetOutput(final long... codes) {
        final long[] copy = Arrays.copyOf(codes, codes.length);
        for (int n = 0; n < 100; n++)
            for (int v = 0; v < 100; v++)
                if (TARGET == gravityAssist(n, v, copy)[0]) return 100 * n + v;
        return -1;
    }

    /**
     * The inputs should still be provided to the program by replacing the values at addresses 1 and 2, just like before.
     * In this program, the value placed in address 1 is called the noun, and the value placed in address 2 is called
     * the verb.
     */
    long[] gravityAssist(final int noun, final int verb, final long... codes) {
        codes[1] = noun;
        codes[2] = verb;
        return Program.runProgram(codes);
    }
}
