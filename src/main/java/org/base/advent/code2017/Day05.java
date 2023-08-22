package org.base.advent.code2017;

import org.base.advent.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * <a href="https://adventofcode.com/2017/day/05">Day 05</a>
 */
public class Day05 implements Solution<List<Integer>> {

    public static final Function<Integer, Integer> NORMAL_JUMP = integer -> 1 + integer;
    public static final Function<Integer, Integer> STRANGE_JUMP = integer -> (integer >= 3) ? integer - 1 : 1 + integer;

    @Override
    public Object solvePart1(final List<Integer> input) {
        return jumpsToEscape(input, NORMAL_JUMP);
    }

    @Override
    public Object solvePart2(final List<Integer> input) {
        return jumpsToEscape(input, STRANGE_JUMP);
    }

    long jumpsToEscape(final List<Integer> input, final Function<Integer, Integer> incrementer) {
        final List<Integer> copy = new ArrayList<>(input);
        int index = 0;
        int jumps = 0;

        while (index >= 0 && index < copy.size()) {
            int offset = copy.get(index);
            int next = index + offset;
            copy.set(index, incrementer.apply(offset));
            index = next;
            ++jumps;
        }

        return jumps;
    }
}
