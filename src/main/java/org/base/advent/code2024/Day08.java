package org.base.advent.code2024;

import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.util.Point;

import java.util.*;
import java.util.function.Function;

import static org.base.advent.util.Point.inGrid;
import static org.base.advent.util.Util.combinations;

/**
 * <a href="https://adventofcode.com/2024/day/8">Day 8</a>
 */
public class Day08 implements Function<List<String>, Pair<Long, Integer>> {
    public record Antinodes(long count, int harmonics) {}

    @Override
    public Pair<Long, Integer> apply(List<String> input) {
        final int size = input.size();
        Map<Character, List<Point>> scan = scanAntennas(input);
        Set<Point> nodes = new HashSet<>(), harmonics = new HashSet<>();
        for (List<Point> antennas : scan.values()) {
            List<List<Point>> pairs = combinations(antennas, 2);
            for (List<Point> pair : pairs) {
                Point a = pair.get(0), b = pair.get(1);
                harmonics.add(a);
                harmonics.add(b);
                long dx1 = a.x - b.x, dy1 = a.y - b.y;
                long dx2 = b.x - a.x, dy2 = b.y - a.y;
                Point next1 = new Point(a.x + dx1, a.y + dy1);
                Point next2 = new Point(b.x + dx2, b.y + dy2);
                nodes.add(next1);
                nodes.add(next2);
                while (inGrid(next1, size, size)) {
                    harmonics.add(next1);
                    next1 = next1.move(dx1, dy1);
                }
                while (inGrid(next2, size, size)) {
                    harmonics.add(next2);
                    next2 = next2.move(dx2, dy2);
                }
            }
        }
        long count = nodes.stream().filter(pt -> inGrid(pt, size, size)).count();
        return Pair.of(count, harmonics.size());
    }

    private Map<Character, List<Point>> scanAntennas(List<String> input) {
        Map<Character, List<Point>> scan = new HashMap<>();
        for (int r = input.size() - 1; r >= 0; r--) {
            for (int c = 0; c < input.size(); c++) {
                char at = input.get(r).charAt(c);
                if (at != '.') {
                    if (!scan.containsKey(at))
                        scan.put(at, new ArrayList<>());
                    scan.get(at).add(new Point(c, r));
                }
            }
        }
        return scan;
    }
}

