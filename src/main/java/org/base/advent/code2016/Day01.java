package org.base.advent.code2016;

import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.util.Point;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * <a href="https://adventofcode.com/2016/day/01">Day 01</a>
 */
public class Day01 implements Function<String, Day01.EasterBunnyHQ> {
    public record EasterBunnyHQ(long distance, long alreadyVisited) {}

    @Override
    public EasterBunnyHQ apply(String input) {
        List<String> list = Arrays.asList(input.split("\\s*,\\s*"));
        Pair<Point, Point> pair = followDirections(list);
        return new EasterBunnyHQ(
                pair.getLeft().getManhattanDistance(), pair.getRight().getManhattanDistance());
    }

    Pair<Point, Point> followDirections(final List<String> list) {
        String[] nesw = new String[] {"^", ">", "v", "<"};
        int facing = 0; // up/north
        final Set<Point> path = new HashSet<>();
        Point point = Point.ORIGIN, alreadyVisited = null;
        path.add(point);

        for (final String instruction : list) {
            final boolean goLeft = instruction.charAt(0) == 'L';
            final int distance = Integer.parseInt(instruction.substring(1));
            facing = (4 + facing + (goLeft ? -1 : 1)) % 4;
            for (int i = 0; i < distance; i++) {
                point = point.move(nesw[facing], 1);
                if (path.contains(point) && alreadyVisited == null)
                    alreadyVisited = point;
                else
                    path.add(point);
            }
        }

        return Pair.of(point, alreadyVisited);
    }
}
