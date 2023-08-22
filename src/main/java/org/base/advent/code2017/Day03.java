package org.base.advent.code2017;

import java.util.LinkedHashMap;
import java.util.Map;
import org.base.advent.util.Point;
import org.base.advent.Solution;

/**
 * <a href="https://adventofcode.com/2017/day/03">Day 03</a>
 */
public class Day03 implements Solution<Integer> {
    @Override
    public Object solvePart1(Integer input) {
        return distanceFromOrigin(input);
    }

    @Override
    public Object solvePart2(Integer input) {
        return firstLargerValue(input);
    }

    long distanceFromOrigin(final int location) {
        return toPoint(location).getManhattanDistance();
    }

    long firstLargerValue(final int input) {
        Map<Point, Long> vmap = new LinkedHashMap<>();
        vmap.put(Point.ORIGIN, 1L);

        long firstLarger = -1;
        int index = 2;

        while (firstLarger <= input) {
            Point point = toPoint(index);
            firstLarger = spiralSum(point, vmap);
            ++index;
        }

        return firstLarger;
    }

    long spiralSum(final Point point) {
        return spiralSum(point, new LinkedHashMap<>());
    }

    long spiralSum(final Point point, final Map<Point, Long> vmap) {
        vmap.put(Point.ORIGIN, 1L);

        if (vmap.containsKey(point))
            return vmap.get(point);

        long sum = -1;
        int index = 2;
        Point target = null;

        while (!point.equals(target)) {
            target = toPoint(index);
            if (!vmap.containsKey(target)) {
                sum = target.surrounding().stream().mapToLong(pt -> vmap.getOrDefault(pt, 0L)).sum();
                vmap.put(target, sum);
            }
            ++index;
        }

        return sum;
    }

    Point toPoint(final int location) {
        if (location == 1)
            return Point.ORIGIN;

        // lower-right corner of each spiral loop is n^2, where n is the loop index + 1 and increments by 2 for each loop
        int loop = 0;
        int index = 1;
        int corner = (int) Math.pow(index, 2);
        while (location > corner) {
            ++loop;
            index = 1 + loop * 2;
            corner = (int) Math.pow(index, 2);
        }

        // check bottom side
        if (location > (corner - index)) {
            return new Point(loop - (corner - location), -loop);
        }
        else {
            corner = corner - index + 1;
            // check left side
            if (location > (corner - index)) {
                return new Point(-loop, -loop + (corner - location));
            }
            else {
                corner = corner - index + 1;
                // check top side
                if (location > (corner - index)) {
                    return new Point(-loop + (corner - location), loop);
                }
                else {
                    corner = corner - index + 1;
                    // check right side
                    if (location > (corner - index)) {
                        return new Point(loop, loop - (corner - location));
                    }
                }
            }
        }

        return Point.ORIGIN;
    }
}
