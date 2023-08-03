package org.base.advent.code2019;

import org.base.advent.Solution;

import java.util.stream.Stream;

/**
 * <a href="https://adventofcode.com/2019/day/16">Day 16</a>
 */
public class Day16 implements Solution<String> {

    @Override
    public String getInput(){
        return readInput("/2019/input16.txt");
    }

    @Override
    public String solvePart1() {
        return transmit(getInput(), 100L);
    }

    @Override
    public String solvePart2() {
        return realSignal(getInput());
    }

    public String realSignal(final String input) {
        final int index = Integer.parseInt(input.substring(0, 7));
        final long[] code = Stream.of(input.repeat(10000).substring(index).split("")).mapToLong(Long::parseLong).toArray();
        long[] state = code.clone();
        final long[] next = new long[code.length];

        for (int p = 0; p < 100; p++) {
            long count = 0;
            for (int location = state.length - 1; location >= 0; location--) {
                count = (count + state[location]) % 10;
                next[location] = count;
            }

            System.arraycopy(next, 0, state, 0, state.length);
        }

        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < 8; i++) result.append(state[i]);
        return result.toString();
    }

    public String transmit(final String input, final long phaseCount) {
        String fft = input;
        for (int p = 0; p < phaseCount; p++) fft = phase(fft);
        return fft.substring(0, 8);
    }

    String phase(final String fft) {
        final StringBuilder result = new StringBuilder();
        final long[] code = Stream.of(fft.split("")).mapToLong(Integer::parseInt).toArray();
        final long[] state = code.clone();
        for (int i = 0, len = fft.length(); i < len; i++) {
            result.append(Math.abs(factor(state, i)) % 10);
        }

        return result.toString();
    }

    long factor(final long[] state, final int iteration) {
        long agg = 0L;
        for (int i = iteration; i < state.length; i++) agg += current(iteration, i) * state[i];
        return agg;
    }

    long current(final long iteration, final long position) {
        if (position < iteration) return 0L;
        final long index = (position - iteration) % (4 * (iteration + 1L));
        final long actual = Math.floorDiv(index, (iteration + 1L));
        switch ((int) actual) {
            case 0: return 1;
            case 2: return -1;
            default: return 0;
        }
    }
}
