package org.base.advent.code2015;

import org.base.advent.Solution;
import org.base.advent.util.Point;

import java.util.HashSet;
import java.util.Set;

/**
 * <a href="https://adventofcode.com/2015/day/03">Day 03</a>
 */
public class Day03 implements Solution<String> {
    @Override
    public Object solvePart1(final String input) {
        Set<Point> presentCount = new HashSet<>();
        followDirections(input, presentCount,0, 1);
        return presentCount.size();
    }

    @Override
    public Object solvePart2(final String input) {
        Set<Point> presentCount = new HashSet<>();
        followDirections(input, presentCount, 0, 2);
        followDirections(input, presentCount, 1, 2);
        return presentCount.size();
    }

    void followDirections(final String directions,
                          final Set<Point> presentCount,
                          final int startIndex,
                          final int increment) {
        // begins by delivering a present to the house at his starting location
        Point position = Point.ORIGIN;
        presentCount.add(position);

        final char[] steps = directions.toCharArray();
        for (int index = startIndex, max = steps.length; index < max; index += increment) {
            switch (steps[index]) {
                case '^' -> position = position.up(1);
                case 'v' -> position = position.down(1);
                case '<' -> position = position.left(1);
                case '>' -> position = position.right(1);
            }

            presentCount.add(position);
        }
    }
}
