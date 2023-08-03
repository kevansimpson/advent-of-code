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
    public Integer getInput() {
        return 265149;
    }

    @Override
    public Object solvePart1() {
        return distanceFromOrigin(getInput());
    }

    @Override
    public Object solvePart2() {
        return firstLargerValue(getInput());
    }

    public int distanceFromOrigin(int location) {
        return toPoint(location).getManhattanDistance();
    }

    public long firstLargerValue(int input) {
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

    public long spiralSum(Point point) {
        return spiralSum(point, new LinkedHashMap<>());
    }

    public long spiralSum(Point point, Map<Point, Long> vmap) {
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

    public Point toPoint(int location) {
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
