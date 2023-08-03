package org.base.advent.code2017;

import org.base.advent.Solution;

import java.util.ArrayList;
import java.util.List;


/**
 * <a href="https://adventofcode.com/2017/day/17">Day 17</a>
 */
public class Day17 implements Solution<Integer> {

    @Override
    public Integer getInput() {
        return 337;
    }

    @Override
    public Object solvePart1() {
        return shortCircuitSpinLock(getInput());
    }

    @Override
    public Object solvePart2() {
        return angrySpinLock(getInput());
    }

    public int angrySpinLock(final int input) {
        int currPos = 0;
        int result = 0;
        final int limit = 50000000;
        int n = 0;
        while (n < limit) {
            if (currPos == 1)
                result = n;
            /*
                How many steps fit between `pos` and the next n to wrap? Call this `fits`.
                Each time we add step_size + 1 steps, so:
                 - pos + step_size * fits + fits >= n + fits
                 - pos + step_size * fits >= n
             */
            final int fits = (n - currPos) / input;
            n += (fits + 1);
            currPos = (currPos + (fits + 1) * (input + 1) - 1) % n + 1;
        }

        return result;
    }

    public int shortCircuitSpinLock(final int steps) {
        final List<Integer> buffer = newBuffer();
        int position = 0;
        for (int i = 1; i < 2018; i++) {
            position = stepForward(buffer, steps, position, i);
            debug(buffer + " @ " + position);
        }

        return buffer.get(buffer.indexOf(2017) + 1);
    }

    public int stepForward(final List<Integer> buffer, final int steps, final int position, final int index) {
        final int size = buffer.size();
        final int next = (position + steps) % size + 1;
        buffer.add(next, index);
        return next;
    }

    public List<Integer> newBuffer() {
        final List<Integer> buffer = new ArrayList<>();
        buffer.add(0);
        return buffer;
    }
}
