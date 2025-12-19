package org.base.advent.code2025;

import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.util.Point;

import java.util.*;
import java.util.function.Function;

import static org.base.advent.util.Point.inGrid;

/**
 * <a href="https://adventofcode.com/2025/day/4">Day 4</a>
 */
public class Day04 implements Function<List<String>, Pair<Integer, Integer>> {
    @Override
    public Pair<Integer, Integer> apply(List<String> input) {
        int height = input.size(), width = input.get(0).length(), rolls = 1;
        Map<Point, Character> grid = new HashMap<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                char ch = input.get(y).charAt(x);
                if (ch == '@')
                    grid.put(new Point(x, y), ch);
            }
        }

        List<Integer> rollCounts = new ArrayList<>();
        while (rolls > 0) {
            Set<Point> set = new HashSet<>();
            for (Map.Entry<Point, Character> entry : grid.entrySet()) {
                Point pt = entry.getKey();
                if (pt.surrounding().stream()
                        .filter(n -> inGrid(n, width, height) && grid.containsKey(n))
                        .count() < 4) {
                    set.add(pt);
                }
            }
            rolls = set.size();
            rollCounts.add(rolls);
            set.forEach(grid::remove);
        }

        return Pair.of(rollCounts.get(0), rollCounts.stream().mapToInt(Integer::intValue).sum());
    }
}

