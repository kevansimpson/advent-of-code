package org.base.advent.code2015;

import lombok.Getter;
import org.base.advent.Solution;
import org.base.advent.util.Point;

import java.util.HashMap;
import java.util.Map;

/**
 * <a href="https://adventofcode.com/2015/day/03">Day 03</a>
 */
public class Day03 implements Solution<String> {

    private static final Integer DELIVERED_PRESENT = 1;

    private final Map<Point, Integer> presentCount = new HashMap<>();

    @Getter
    private final String input = readInput("/2015/input03.txt");

    @Override
    public Object solvePart1() {
        followDirections(getInput(), 0, 1);
        return presentCount.size();
    }

    @Override
    public Object solvePart2() {
        presentCount.clear();
        final String directions = getInput();
        followDirections(directions, 0, 2);
        followDirections(directions, 1, 2);
        return presentCount.size();
    }

    protected void followDirections(final String directions, final int startIndex, final int increment) {
        // begins by delivering a present to the house at his starting location
        Point position = Point.ORIGIN;
        presentCount.put(position, DELIVERED_PRESENT);

        final char[] steps = directions.toCharArray();
        for (int index = startIndex, max = steps.length; index < max; index += increment) {
            switch (steps[index]) {
                case '^' -> position = position.up(1);
                case 'v' -> position = position.down(1);
                case '<' -> position = position.left(1);
                case '>' -> position = position.right(1);
            }

            presentCount.put(position, DELIVERED_PRESENT);
        }
    }
}
