package org.base.advent.code2017;

import lombok.Getter;
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

    /*
        toList() seems to return unmodifiable Collection, but Intellij still warns about not using longer syntax.
        wrapping unmodifiable from toList() in new ArrayList() addresses the issue, perhaps not so elegantly?
     */
    @Getter
    private final List<Integer> input = new ArrayList<>(readLines("/2017/input05.txt")
            .stream().map(Integer::parseInt).toList());
    @Getter
    private final List<Integer> input2 = new ArrayList<>(readLines("/2017/input05.txt")
            .stream().map(Integer::parseInt).toList());

    @Override
    public Object solvePart1() {
        return jumpsToEscape(getInput(), NORMAL_JUMP);
    }

    @Override
    public Object solvePart2() {
        return jumpsToEscape(getInput2(), STRANGE_JUMP);
    }

    public long jumpsToEscape(List<Integer> input, Function<Integer, Integer> incrementer) {
        int index = 0;
        int jumps = 0;

        while (index >= 0 && index < input.size()) {
            int offset = input.get(index);
            int next = index + offset;
            input.set(index, incrementer.apply(offset));
            index = next;
            ++jumps;
        }

        return jumps;
    }
}
