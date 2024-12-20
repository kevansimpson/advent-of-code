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
    @Override
    public Object solvePart1(final List<String[]> input) {
        return closestIntersection(input);
    }

    @Override
    public Object solvePart2(final List<String[]> input) {
        return fewestSteps(input);
    }

    // expects and uses only 2 wires
    long fewestSteps(final List<String[]> jumbledWires) {
        final Pair<WirePath, WirePath> untangled = untangle(jumbledWires);
        return analyzePath(untangled, pt -> countStepsAt(pt, untangled));
    }

    // expects and uses only 2 wires
    long closestIntersection(final List<String[]> jumbledWires) {
        return analyzePath(untangle(jumbledWires), Point::getManhattanDistance);
    }

    long analyzePath(Pair<WirePath, WirePath> untangled, final Function<Point, Long> function) {
        return SetUtils.intersection(
                new HashSet<>(untangled.getLeft().getPath()), new HashSet<>(untangled.getRight().getPath()))
                .stream()
                .map(function)
                .min(Comparator.naturalOrder()).orElse(-1L);
    }

    long countStepsAt(final Point point, final Pair<WirePath, WirePath> untangled) {
        return untangled.getLeft().getSteps().getOrDefault(point, 0) +
                untangled.getRight().getSteps().getOrDefault(point, 0);
    }

    Pair<WirePath, WirePath> untangle(final List<String[]> jumbles) {
        final List<WirePath> untangled = jumbles.stream().map(this::runWires).toList();
        return Pair.of(untangled.get(0), untangled.get(1));
    }

    WirePath runWires(final String... wire) {
        final List<Point> path = new LinkedList<>();
        final Map<Point, Integer> steps = new HashMap<>();
        Point pt = Point.ORIGIN;
        int count = 0;
        for (final String w : wire) {
            final String dir = w.substring(0, 1);
            final int dist = Integer.parseInt(w.substring(1));
            for (int x = 1; x <= dist; x++) {
                pt = pt.move(dir);
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
