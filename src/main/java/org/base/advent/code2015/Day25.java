package org.base.advent.code2015;

import org.base.advent.Solution;
import org.base.advent.util.Point;

/**
 * <a href="https://adventofcode.com/2015/day/25">Day 25</a>
 */
public class Day25 implements Solution<Point> {

    @Override
    public Object solvePart1(Point input) {
        return readMachineConsole(input);
    }

    @Override
    public Object solvePart2(Point input) {
        return 1138;
    }

    long readMachineConsole(final Point target) {
        int index = target.iy() - 1;
        for (int i = 0; i < (target.ix() + target.iy() - 1); i++) {
            index += i;
        }

        long next = 20151125L;
        for (int i = 0; i < index; i++) {
            next = next(next);
        }

        return next;
    }

    long next(final long start) {
        return (start * 252533) % 33554393;
    }
}