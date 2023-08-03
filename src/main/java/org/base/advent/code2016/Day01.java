package org.base.advent.code2016;

import lombok.Getter;
import org.base.advent.Solution;
import org.base.advent.util.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * <a href="https://adventofcode.com/2016/day/01">Day 01</a>
 */
public class Day01 implements Solution<List<String>> {

    @Override
    public List<String> getInput(){
        return Arrays.asList(readInput("/2016/input01.txt").split("\\s*,\\s*"));
    }

    @Override
    public Object solvePart1() {
        return calculateDistance(getInput(), false);
    }

    @Override
    public Object solvePart2() {
        return calculateDistance(getInput(), true);
    }

    public int calculateDistance(final List<String> list, final boolean stopOn2ndVisit) {
        return followDirections(list, stopOn2ndVisit).getManhattanDistance();
    }

    private enum Dir {
        up(0, 1),       // (left, right)
        left(-1, 0),    // (down, up)
        down(0, -1),    // (right, left)
        right(1, 0);    // (up, down)

        @Getter
        private final int deltaX;
        @Getter
        private final int deltaY;

        Dir(final int dx, final int dy) {
            deltaX = dx;
            deltaY = dy;
        }

        public Dir next(final boolean goLeft) {
            switch (this) {
                case up: return goLeft ? left : right;
                case left: return goLeft ? down : up;
                case down: return goLeft ? right : left;
                case right: return goLeft ? up : down;
                default:
                    throw new IllegalArgumentException(this.toString());
            }
        }
    }

    Point followDirections(final List<String> list, final boolean stopOn2ndVisit) {
        final List<Point> path = new ArrayList<>();
        Point point = Point.ORIGIN;
        path.add(point);

        Dir direction = Dir.up;
        for (final String instruction : list) {
            final boolean goLeft = instruction.charAt(0) == 'L';
            final int distance = Integer.parseInt(instruction.substring(1));
            direction = direction.next(goLeft);
            final List<Point> jump = gather(last(path), direction.getDeltaX(), direction.getDeltaY(), distance);
            if (stopOn2ndVisit) {
                for (final Point pt : jump) {
                    if (path.contains(pt))
                        return pt;
                    else
                        path.add(pt);
                }
            }
            else
                path.addAll(jump);
        }

        return last(path); // last stop
    }

    static List<Point> gather(Point start, int deltaX, int deltaY, int distance) {
        final List<Point> path = new ArrayList<>();
        for (int i = 1; i <= distance; i++)
            path.add(start.move(i * deltaX, i * deltaY));

        return path;
    }

    private static Point last(final List<Point> list) {
        return list.get(list.size() - 1);
    }
}
