package org.base.advent.code2016;

import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.util.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.lang.Long.toBinaryString;
import static org.apache.commons.lang3.StringUtils.countMatches;

/**
 * <a href="https://adventofcode.com/2016/day/13">Day 13</a>
 */
public class Day13 implements Function<Integer, Pair<Integer, Long>> {
    private static final int WALL = -1;
    private static final Point TARGET = Point.of(31,39);

    @Override
    public Pair<Integer, Long> apply(Integer input) {
        int depth = - 1;
        final Map<Point, Integer> depthMap = new HashMap<>();
        final List<Point> search = new ArrayList<>(List.of(Point.of(1, 1)));

        maze: while (!search.isEmpty()) {
            depth++;
            List<Point> current = new ArrayList<>(search);
            search.clear();

            for (Point pt : current) {
                if (depthMap.containsKey(pt))
                    continue;
                else
                    depthMap.put(pt, isWall(pt, input) ? WALL : depth);

                if (TARGET.equals(pt))
                    break maze;

                List<Point> next = pt.cardinal().stream()
                        .filter(n -> isOpenSpace(n, input)).toList();
                search.addAll(next);
            }
        }

        return Pair.of(depth, depthMap.values().stream().filter(d -> d <= 50).count());
    }

    boolean isOpenSpace(Point pt, int input) {
        return pt.x >= 0 && pt.y >= 0 && !isWall(pt, input);
    }
    boolean isWall(Point pt, int input) {
        return (countMatches(toBinary(pt, input), "1") % 2) != 0;
    }
    String toBinary(Point pt, int input) {
        return toBinaryString(pt.x * pt.x + 3 * pt.x + 2 * pt.x * pt.y + pt.y + pt.y * pt.y + input);
    }
}