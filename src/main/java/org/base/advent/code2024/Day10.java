package org.base.advent.code2024;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.util.Point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.base.advent.util.Point.inGrid;
import static org.base.advent.util.Util.safeGet;

/**
 * <a href="https://adventofcode.com/2024/day/10">Day 10</a>
 */
@AllArgsConstructor
public class Day10 implements Function<List<String>, Pair<Integer, Integer>> {
    private final ExecutorService pool;

    @Override
    public Pair<Integer, Integer> apply(List<String> input) {
        final int size = input.size();
        List<CompletableFuture<Pair<Integer, Integer>>> trails = new ArrayList<>();
        for (int r = size - 1; r >= 0; r--) {
            for (int c = 0; c < size; c++) {
                if (input.get(r).charAt(c) == '0') {
                    final Point head = new Point(c, size - r - 1);
                    trails.add(supplyAsync(() -> ascendTrail(head, input, size), pool));
                }
            }
        }

        int sum = 0, rating = 0;
        for (CompletableFuture<Pair<Integer, Integer>> cf : trails) {
            Pair<Integer, Integer> t = safeGet(cf);
            sum += t.getLeft();
            rating += t.getRight();
        }
        return Pair.of(sum, rating);
    }

    private Pair<Integer, Integer> ascendTrail(Point head, List<String> allTrails, final int size) {
        int height = 0;
        List<Point> steps = List.of(head);
        while (!steps.isEmpty() && height < 9) {
            final char h = Character.forDigit(++height, 10);
            List<Point> next = new ArrayList<>();
            for (Point step : steps) {
                step.cardinal().stream()
                        .filter(pt ->
                                inGrid(pt, size, size) &&
                                        allTrails.get(size - pt.iy() - 1).charAt(pt.ix()) == h)
                        .forEach(next::add);
            }
            steps = next;
        }

        return Pair.of(new HashSet<>(steps).size(), steps.size());
    }
}

