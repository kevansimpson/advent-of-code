package org.base.advent.code2022;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.Solution;
import org.base.advent.util.Point;
import org.base.advent.util.SafeLazyInitializer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.base.advent.util.Util.columns;

/**
 * <a href="https://adventofcode.com/2022/day/08">Day 08</a>
 * <p>
 *     Versus:
 *     <a href="https://github.com/kevansimpson/advent-of-kotlin/blob/main/src/main/kotlin/org/base/advent/k2022/Day08.kt">
 *         Kotlin Day 08</a>
 * </p>
 */
public class Day08 implements Solution<List<String>> {
    @Getter @Setter
    private List<String> input = readLines("/2022/input08.txt");

    private final SafeLazyInitializer<Grid> grid = new SafeLazyInitializer<>(() -> {
        List<List<Integer>> rows = getInput().stream()
                .map(it -> Arrays.stream(it.split(""))
                        .mapToInt(Integer::parseInt).boxed().toList()).toList();
        List<List<Integer>> cols = columns(getInput()).stream()
                .map(it -> Arrays.stream(it.split(""))
                        .mapToInt(Integer::parseInt).boxed().toList()).toList();
        int size = getInput().size();
        // this is what flatMapIndexed looks like in Java :-(
        // map the range of indices from getInput() to List<List<Pair<Point, Character>>>
        Map<Point, Integer> area = IntStream.range(0, size).mapToObj(row -> {
            String line = getInput().get(row);
            return IntStream.range(0, line.length()) // map range of line indices to List<Pair<Point, Char...
                    .mapToObj(col -> Pair.of(
                            new Point(col, row),
                            Integer.parseInt(String.valueOf(line.charAt(col))))).collect(Collectors.toList());
            // then flatten the List<List<... and collect into a Map<Point, Integer>
        }).flatMap(Collection::stream).collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
        // trim the outside border to get the "inside"
        List<Map.Entry<Point, Integer>> inside = area.entrySet().stream()
                .filter(it -> isInside(it.getKey(), size - 1)).toList();
        return new Grid(rows, cols, size, area, inside);
    });

    @Override
    public Object solvePart1() {
        return grid.get().visibleTreesFromOutside();
    }

    @Override
    public Object solvePart2() {
        return grid.get().highestScenicScore();
    }

    private boolean isInside(Point pt, int size) {
        return pt.x > 0 && pt.x < size && pt.y > 0 && pt.y < size;
    }

    public record Grid(List<List<Integer>> rows,
                       List<List<Integer>> cols,
                       int size,
                       Map<Point, Integer> area,
                       List<Map.Entry<Point, Integer>> inside) {
        public int visibleTreesFromOutside() {
            return (4 * size - 4) + (int) inside.stream().filter(this::isTreeVisible).count();
        }

        public int highestScenicScore() {
            return inside.stream().mapToInt(this::highScore).max().orElse(1138);
        }

        int highScore(Map.Entry<Point, Integer> entry) {
            Point pt = entry.getKey();
            int height = entry.getValue();
            List<Integer> row = rows.get(pt.y);
            List<Integer> col = cols.get(pt.x);

            List<Integer> left = fromLeft(row, pt).collect(Collectors.toList());
            Collections.reverse(left);
            List<Integer> top = fromTop(col, pt).collect(Collectors.toList());
            Collections.reverse(top);
            return look(left, height) *
                    look(fromRight(row, pt).toList(), height) *
                    look(top, height) *
                    look(fromBottom(col, pt).toList(), height);
        }

        boolean isTreeVisible(Map.Entry<Point, Integer> entry) {
            Point pt = entry.getKey();
            int height = entry.getValue();
            List<Integer> row = rows.get(pt.y);
            List<Integer> col = cols.get(pt.x);
            return (height > fromLeft(row, pt).mapToInt(Integer::valueOf).max().orElse(1138) ||
                    height > fromRight(row, pt).mapToInt(Integer::valueOf).max().orElse(1138) ||
                    height > fromTop(col, pt).mapToInt(Integer::valueOf).max().orElse(1138) ||
                    height > fromBottom(col, pt).mapToInt(Integer::valueOf).max().orElse(1138));
        }

        int look(List<Integer> list, int height) {
            int sub = (int) list.stream().takeWhile(tree -> height > tree).count();
            if (list.size() == sub)
                return sub;
            else
                return sub + 1;
        }

        Stream<Integer> fromLeft(List<Integer> row, Point pt) {
            return row.subList(0, pt.x).stream();
        }
        Stream<Integer> fromRight(List<Integer> row, Point pt) {
            return row.subList(pt.x + 1, row.size()).stream();
        }
        Stream<Integer> fromTop(List<Integer> col, Point pt) {
            return col.subList(0, pt.y).stream();
        }
        Stream<Integer> fromBottom(List<Integer> col, Point pt) {
            return col.subList(pt.y + 1, col.size()).stream();
        }
    }
}
