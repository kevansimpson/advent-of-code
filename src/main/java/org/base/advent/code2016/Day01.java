package org.base.advent.code2016;

import lombok.Getter;
import org.base.advent.Solution;
import org.base.advent.util.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * <h2>Part 1</h2>
 * You're airdropped near Easter Bunny Headquarters in a city somewhere. "Near", unfortunately,
 * is as close as you can get - the instructions on the Easter Bunny Recruiting Document the Elves
 * intercepted start here, and nobody had time to work them out further.
 *
 * The Document indicates that you should start at the given coordinates (where you just landed) and
 * face North. Then, follow the provided sequence: either turn left (L) or right (R) 90 degrees, then
 * walk forward the given number of blocks, ending at a new intersection.
 *
 * There's no time to follow such ridiculous instructions on foot, though, so you take a moment and
 * work out the destination. Given that you can only walk on the street grid of the city, how far is
 * the shortest path to the destination?
 *
 * For example:
 * <pre>
 *     Following R2, L3 leaves you 2 blocks East and 3 blocks North, or 5 blocks away.
 *     R2, R2, R2 leaves you 2 blocks due South of your starting position, which is 2 blocks away.
 *     R5, L5, R5, R3 leaves you 12 blocks away.
 * </pre>
 *
 * How many blocks away is Easter Bunny HQ?

 * <h2>Part 2</h2>
 * Then, you notice the instructions continue on the back of the Recruiting Document.
 * Easter Bunny HQ is actually at the first location you visit twice.
 *
 * For example, if your instructions are R8, R4, R4, R8, the first location you visit twice
 * is 4 blocks away, due East.
 *
 * How many blocks away is the first location you visit twice?
 */
public class Day01 implements Solution<List<String>> {

    @Override
    public List<String> getInput() throws IOException {
        return Arrays.asList(readInput("/2016/input01.txt").split("\\s*,\\s*"));
    }

    @Override
    public Object solvePart1() throws Exception {
        return calculateDistance(getInput(), false);
    }

    @Override
    public Object solvePart2() throws Exception {
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
