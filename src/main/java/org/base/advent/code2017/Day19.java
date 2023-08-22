package org.base.advent.code2017;

import org.apache.commons.lang3.StringUtils;
import org.base.advent.Helpers;
import org.base.advent.util.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <a href="https://adventofcode.com/2017/day/19">Day 19</a>
 */
public class Day19 implements Function<List<String>, Day19.LittlePacket>, Helpers {
    public record LittlePacket(String letters, long steps) {}

    @Override
    public Day19.LittlePacket apply(List<String> input) {
        Map<Point, String> grid = buildGrid(input);
        final List<Point> points = followPath(grid);
        return new Day19.LittlePacket(toLetters(points, grid), points.size());
    }

    List<Point> followPath(final Map<Point, String> grid) {
        final Point start = findStart(grid);
        final List<Point> path = new ArrayList<>();
        path.add(start);

        Point next = start;
        int dX, dY;
        while (next != null) {
            final List<Point> cardinal = next.cardinal().stream()
                    .filter(pt -> grid.get(pt) != null)
                    .filter(pt -> !path.contains(pt))
                    .toList();

            if (cardinal.isEmpty())
                break;
            else if (cardinal.size() == 1) {
                Point point = cardinal.get(0);
                dX = point.ix() - next.ix();
                dY = point.iy() - next.iy();
                while (point != null) {
                    final String marker = grid.get(point);
                    if (marker == null)
                        break;
                    else if (StringUtils.equals("+", marker)) {
                        debug("turn %s", point);
                        path.add(point);
                        next = point;
                        break;
                    }
                    else {
                        debug("add  %s", point);
                        path.add(point);
                        point = point.right(dX).up(dY);
                    }
                }
            }
        }

        return path;
    }

    String toLetters(final List<Point> path, final Map<Point, String> grid) {
        return path.stream()
                .map(grid::get)
                .collect(Collectors.joining())
                .replaceAll("-", "")
                .replaceAll("\\|", "")
                .replaceAll("\\+", "");
    }

    Point findStart(final Map<Point, String> grid) {
        return grid.entrySet().stream().filter(e -> e.getKey().y == 0).findFirst().orElseThrow().getKey();
    }

    Map<Point, String> buildGrid(final List<String> points) {
        final Map<Point, String> grid = new HashMap<>();

        for (int y = 0; y < points.size(); y++) {
            final char[] line = points.get(y).toCharArray();
            for (int x = 0; x < line.length; x++) {
                if (line[x] != ' ')
                    grid.put(new Point(x, -y), String.valueOf(line[x]));
            }
        }

        return grid;
    }
}
