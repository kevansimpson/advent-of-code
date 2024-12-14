package org.base.advent.code2024;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.util.Point;

import java.util.*;
import java.util.function.Function;

import static org.base.advent.util.Point.inGrid;

/**
 * <a href="https://adventofcode.com/2024/day/12">Day 12</a>
 */
public class Day12 implements Function<List<String>, Pair<Integer, Integer>> {
    @Override
    public Pair<Integer, Integer> apply(List<String> input) {
        final int size = input.size();
        boolean[][] visited = new boolean[size][size];
        int perimeterCost = 0, sidesCost = 0;
        for (int r = size - 1; r >= 0; r--) {
            for (int c = 0; c < size; c++) {
                if (!visited[c][size - r - 1]) {
                    Region region = mapRegion(
                            input.get(r).charAt(c), Point.of(c, size - r - 1), visited, input, size);
                    perimeterCost += regionCost(region, size);
                    sidesCost += sidesCost(region, size);
                    for (Point pt : region.plot())
                        visited[pt.ix()][pt.iy()] = true;
                }
            }
        }

        return Pair.of(perimeterCost, sidesCost);
    }

    private int regionCost(Region region, int size) {
        int perimeter = 0;
        for (Point pt : region.plot())
            for (Point c : pt.cardinal())
                if (!inGrid(c, size, size) || !region.plot().contains(c))
                    perimeter++;

        return perimeter * region.plot().size();
    }

    /**
     * <p>
     * Counting the vertical lines in one sweep, and the horizontal lines in another.
     * The key things to avoid are 1) counting interior lines and 2) counting existing lines.
     * You can avoid 1 by canceling out the left and right sides of adjacent plots,
     * and you can avoid 2 by keeping tracking of the prior row's lines.
     * </p>
     * h/t <a href="https://www.reddit.com/user/abnew123/">Reddit: abnew123</a>
     */
    private int sidesCost(Region region, int size) {
        int sides = 0;
        Set<Integer> vertical = new HashSet<>();
        for (int i = 0; i < size; i++) {
            Set<Integer> vert1 = new HashSet<>();
            for (Point pt : region.plot())
                if (pt.ix() == i) {
                    vert1.add(-1 * (pt.iy() + 1));
                    vert1.add(pt.iy() + 2);
                }

            Set<Integer> vert2 = new HashSet<>();
            for (Integer v : vert1)
                if (!vert1.contains(-1 * v))
                    vert2.add(v);

            sides += CollectionUtils.removeAll(vert2, vertical).size();
            vertical = vert2;
        }

        Set<Integer> horizontal = new HashSet<>();
        for (int i = 0; i < size; i++) {
            Set<Integer> horiz1 = new HashSet<>();
            for (Point pt : region.plot())
                if (pt.iy() == i) {
                    horiz1.add(-1 * (pt.ix() + 1));
                    horiz1.add(pt.ix() + 2);
                }

            Set<Integer> horiz2 = new HashSet<>();
            for (Integer h : horiz1)
                if (!horiz1.contains(-1 * h))
                    horiz2.add(h);

            sides += CollectionUtils.removeAll(horiz2, horizontal).size();
            horizontal = horiz2;
        }

        return sides * region.plot().size();
    }

    private Region mapRegion(char plant, Point start, boolean[][] visited, List<String> input, int size) {
        Region region = new Region(plant, new HashSet<>());
        List<Point> plot = new ArrayList<>();
        plot.add(start);
        while (!plot.isEmpty()) {
            for (Point v : plot) {
                region.plot().add(v);
                visited[v.ix()][v.iy()] = true;
            }
            List<Point> next = new ArrayList<>();
            for (Point pt : plot) {
                for (Point c : pt.cardinal()) {
                    if (inGrid(c, size, size) &&
                            plant == input.get(size - c.iy() - 1).charAt(c.ix()) &&
                            !visited[c.ix()][c.iy()] &&
                            !next.contains(c))
                        next.add(c);
                    }
            }
            plot = next;
        }
        return region;
    }

    record Region(char plant, Set<Point> plot) {}
}

