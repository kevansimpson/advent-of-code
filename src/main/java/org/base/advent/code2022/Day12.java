package org.base.advent.code2022;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.Solution;
import org.base.advent.TimeSaver;
import org.base.advent.util.Node;
import org.base.advent.util.Point;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static org.base.advent.util.Node.createRootNode;
import static org.base.advent.util.Point.inGrid;

/**
 * <a href="https://adventofcode.com/2022/day/12">Day 12</a>
 */
public class Day12 implements Solution<List<String>> {
    @Getter
    private final List<String> input = readLines("/2022/input12.txt");
    private final Hill localArea = mapHill(getInput());

    @Override
    public Object solvePart1() {
        return climb(localArea);
    }

    @Override
    public Object solvePart2() {
        return localArea.heightmap.entrySet().parallelStream()
                .filter(it -> it.getValue() == 'a')
                .map(Map.Entry::getKey)
                .mapToLong(it -> climb(localArea.copy(it)))
                .min().orElse(1138L);
    }

    long climb(Hill hill) {
        List<Node<Point>> nodes = new ArrayList<>();
        nodes.add(createRootNode(hill.start));
        Map<Point, Long> depthMap = new HashMap<>();
        AtomicLong minimum = new AtomicLong(Long.MAX_VALUE);
        AtomicLong searchDepth = new AtomicLong(-1);

        while (!nodes.isEmpty()) {
            List<Node<Point>> current = new ArrayList<>(nodes);
            nodes.clear();
            long depth = searchDepth.incrementAndGet();
            current.forEach(node -> {
                Point last = node.getData();
                if (!depthMap.containsKey(last)) {
                    depthMap.put(last, depth);
                    if (last.equals(hill.peak))
                        minimum.set(depth);
                    else
                        nodes.addAll(last.cardinal().stream()
                                .filter(it -> depthMap.getOrDefault(it, Long.MAX_VALUE) >= depth &&
                                        !node.contains(it) &&
                                        inGrid(it, hill.width, hill.length) &&
                                        hill.isHigher(last, it))
                                .map(node::addChild)
                                .toList());
                }
            });
        }

        return minimum.get();
    }

    Hill mapHill(List<String> view) {
        Map<Point, Character> heightmap = new HashMap<>();
        String line = "";
        for (int y = 0, maxY = view.size(); y < maxY; y++) {
            line = view.get(y);
            for (int x = 0, maxX = line.length(); x < maxX; x++)
                heightmap.put(Point.of(x, y), line.charAt(x));
        }

        return new Hill(heightmap, heightmap.size(), line.length());
    }

    @AllArgsConstructor
    private class Hill implements TimeSaver {
        private static final int A = 'a';
        private static final int Z = 'z';
        private static final int PEAK = Z + 1;

        private final Map<Point, Character> heightmap;
        private final Map<Point, Integer> numberMap;
        private final int length;
        private final int width;
        private final Point start;
        private final Point peak;

        public Hill(Map<Point, Character> heights, int l, int w) {
            heightmap = heights;
            length = l;
            width = w;
            start = find('S');
            peak = find('E');
            numberMap = heights.entrySet().stream()
                    .map(it -> Pair.of(it.getKey(), (int) it.getValue()))
                    .collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
            numberMap.put(start, A);
            numberMap.put(peak, PEAK);

            if (isFullSolve())
                graph();
            debug("%s --> %s", start, peak);
        }

        public Hill copy(Point altStart) {
            return new Hill(heightmap, numberMap, length, width, altStart, peak);
        }

        public boolean isHigher(Point here, Point next) {
            return Range.of(A, (numberMap.get(here) + 1)).contains(numberMap.get(next));
        }

        public void graph() {
            for (int y = 0; y < length; y++) {
                for (int x = 0; x < width; x++)
                    System.out.print(heightmap.getOrDefault(Point.of(x, y), '?'));
                System.out.println();
            }
        }

        private Point find(Character ch) {
            return heightmap.entrySet().stream()
                    .filter(it -> it.getValue() == ch)
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElseThrow();
        }
    }
}
