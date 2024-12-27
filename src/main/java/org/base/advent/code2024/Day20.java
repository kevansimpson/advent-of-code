package org.base.advent.code2024;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.util.Node;
import org.base.advent.util.Point;
import org.base.advent.util.Point.Dir;

import java.util.*;
import java.util.function.Function;

/**
 * <a href="https://adventofcode.com/2024/day/20">Day 20</a>
 */
@AllArgsConstructor
public class Day20 implements Function<List<String>, Pair<Integer, Integer>> {
    private final int threshold;

    @Override
    public Pair<Integer, Integer> apply(List<String> input) {
        Map<Point, Long> visited = createCourse(input).findPath();
        // Create a list from elements of HashMap
        List<Map.Entry<Point, Long> > list = new ArrayList<>(visited.entrySet());
        // Sort the list
        list.sort(Map.Entry.comparingByValue());
        int part1 = 0, part2 = 0;
        for (int p = 0; p < list.size() - 1; p++) {
            Map.Entry<Point, Long> entry = list.get(p);
            for (int i = list.size() - 1; i >= p + 1; i--) {
                Map.Entry<Point, Long> last = list.get(i);
                long md = entry.getKey().getManhattanDistance(last.getKey());
                if (md <= 20) {
                    long diff = Math.abs((entry.getValue() + md) - last.getValue());
                    if (diff >= threshold) {
                        part2++;
                        if (md == 2L)
                            part1++;
                    }
                }
            }
        }

        return Pair.of(part1, part2);
    }

    record Course(char[][] course, int size, Point start, Point end) {

        Map<Point, Long> findPath() {
            Map<Point, Long> visited = new HashMap<>();
            LinkedList<Node<Point>> paths = new LinkedList<>();
            paths.add(Node.createRootNode(start));

            while (!paths.isEmpty()) {
                Node<Point> node = paths.removeFirst();
                Point pos = node.getData();
                if (!visited.containsKey(pos)) {
                    visited.put(pos, node.getDepth());
                    if (pos.equals(end)) {
                        return visited;
                    }
                    else {
                        for (Dir dir : Dir.values()) {
                            Point next = pos.move(dir);
                            if (!visited.containsKey(next) && get(next) != '#')
                                paths.add(node.addChild(next));
                        }
                    }
                }
            }
            return visited;
        }

        char get(Point pos) {
            return course()[pos.iy()][pos.ix()];
        }
    }

    Course createCourse(List<String> input) {
        final int size = input.size();
        char[][] course = new char[size][size];
        Point start = null, end = null;
        for (int r = size - 1; r >= 0; r--) {
            int row = size - r - 1;
            for (int c = 0; c < size; c++) {
                char at = input.get(row).charAt(c);
                course[row][c] = at;
                switch (at) {
                    case 'S' -> start = new Point(c, row);
                    case 'E' -> end = new Point(c, row);
                }
            }
        }

        return new Course(course, size, start, end);
    }
}

