package org.base.advent.code2019;

import lombok.Getter;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.Solution;
import org.base.advent.util.Point;

import java.util.*;
import java.util.function.Function;

/**
 * <a href="https://adventofcode.com/2019/day/03">Day 03</a>
 */
public class Day03 implements Solution<List<String[]>> {
    @Getter
    private final List<String[]> input =  readCSVLines("/2019/input03.txt");

    @Override
    public Object solvePart1() {
        return closestIntersection(getInput());
    }

    @Override
    public Object solvePart2() {
        return fewestSteps(getInput());
    }

    // expects and uses only 2 wires
    public int fewestSteps(final List<String[]> jumbledWires) {
        final Pair<WirePath, WirePath> untangled = untangle(jumbledWires);
        return analyzePath(untangled, pt -> countStepsAt(pt, untangled));
    }

    // expects and uses only 2 wires
    public int closestIntersection(final List<String[]> jumbledWires) {
        return analyzePath(untangle(jumbledWires), Point::getManhattanDistance);
    }

    protected int analyzePath(Pair<WirePath, WirePath> untangled, final Function<Point, Integer> function) {
        return SetUtils.intersection(
                new HashSet<>(untangled.getLeft().getPath()), new HashSet<>(untangled.getRight().getPath()))
                .stream()
                .map(function)
                .min(Comparator.naturalOrder()).orElse(-1);
    }

    protected int countStepsAt(final Point point, final Pair<WirePath, WirePath> untangled) {
        return untangled.getLeft().getSteps().getOrDefault(point, 0) +
                untangled.getRight().getSteps().getOrDefault(point, 0);
    }

    protected Pair<WirePath, WirePath> untangle(final List<String[]> jumbles) {
        final List<WirePath> untangled = jumbles.stream().map(this::runWires).toList();
        return Pair.of(untangled.get(0), untangled.get(1));
    }

    protected WirePath runWires(final String... wire) {
        final List<Point> path = new LinkedList<>();
        final Map<Point, Integer> steps = new HashMap<>();
        Point pt = Point.ORIGIN;
        int count = 0;
        for (final String w : wire) {
            final String dir = w.substring(0, 1);
            final int dist = Integer.parseInt(w.substring(1));
            for (int x = 1; x <= dist; x++) {
                pt = Point.MOVE_MAP.get(dir).apply(pt);
                path.add(pt);
                ++count;
                if (!steps.containsKey(pt)) steps.put(pt, count);
            }
        }

        return new WirePath(path, steps);
    }

    @Getter
    private static class WirePath {
        private final List<Point> path;
        private final Map<Point, Integer> steps;

        public WirePath(final List<Point> p, final Map<Point, Integer> s) {
            path = p;
            steps = s;
        }
    }
}
