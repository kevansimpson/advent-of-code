package org.base.advent.code2022;

import org.base.advent.TimeSaver;
import org.base.advent.util.Point;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.lang.Math.*;

/**
 * <a href="https://adventofcode.com/2022/day/14">Day 14</a>
 */
public class Day14 implements Function<List<String>, Day14.PouredSand>, TimeSaver {
    public record PouredSand(long noFloor, long withFloor) {}

    private static final String ROCK = "#";
    private static final String SAND = "o";
    private static final String AIR = " ";
    private static final String SRC = "+";
    private static final Point DROP = Point.of(500, 0);

    @Override
    public PouredSand apply(final List<String> input) {
        final Cave cave1 = buildCave(input.stream().map(this::scanRocks).toList());
        final Cave cave2 = cave1.addFloor();
        return new PouredSand(pourSand(cave1), pourSand(cave2));
    }

    long pourSand(final Cave cave) {
        Point sand = dropSand(cave, DROP);
        while (sand != null) {
            cave.drop(sand);
            sand = (DROP.equals(sand)) ? null : dropSand(cave, DROP);
        }
        if (isFullSolve())
            cave.display();

        return cave.countSand();
    }

    Point dropSand(final Cave cave, final Point drop) {
        Point[] below = new Point[] {
                drop.move(-1, 1), drop.move(0, 1), drop.move(1, 1)};
        String space = Stream.of(below).map(cave::at).collect(Collectors.joining());
        if (drop.y >= cave.floor)
            return null;
        else if (AIR.equals(cave.at(below[1])))
            return dropSand(cave, below[1]);
        else if (AIR.equals(cave.at(below[0])))
            return dropSand(cave, below[0]);
        else if (AIR.equals(cave.at(below[2])))
            return dropSand(cave, below[2]);
        else if (space.trim().length() == 3)
            return drop;
        else
            return null;
    }

    List<Point> rockPath(final Point start, final Point end) {
        long dx = abs(start.x - end.x);
        if (dx == 0L) {
            return LongStream.rangeClosed(min(start.y, end.y), max(start.y, end.y))
                    .mapToObj(y -> Point.of(start.x, y)).toList();
        }

        return LongStream.rangeClosed(min(start.x, end.x), max(start.x, end.x))
                .mapToObj(x -> Point.of(x, start.y)).toList();
    }

    private Cave buildCave(final List<List<Point>> paths) {
        Map<Point, String> rocks = new HashMap<>(Map.of(DROP, SRC));
        paths.forEach(path -> {
            for (int i = 0, max = path.size() - 1; i < max; i++)
                for (Point pt : rockPath(path.get(i), path.get(i + 1)))
                    rocks.put(pt, ROCK);
        });

        return new Cave(rocks);
    }

    private List<Point> scanRocks(final String str) {
        return Stream.of(str.split(" -> ")).map(Point::point).toList();
    }

    private static class Cave {
        private final Map<Point, String> rocks;
        private final int left;
        private final int right;
        private final int top;
        private final int floor;

        public Cave(Map<Point, String> someRocks) {
            rocks = someRocks;
            int leftRock = rocks.keySet().parallelStream().mapToInt(Point::ix).min().orElseThrow();
            int rightRock = rocks.keySet().parallelStream().mapToInt(Point::ix).max().orElseThrow();
            int width = (rightRock - leftRock) * 3;
            left = leftRock - width;
            right = rightRock + width;
            top = min(0, rocks.keySet().parallelStream().mapToInt(Point::iy).min().orElseThrow());
            int bottom = rocks.keySet().parallelStream().mapToInt(Point::iy).max().orElseThrow();
            floor = 2 + bottom;
        }

        public String at(Point pt) {
            return rocks.getOrDefault(pt, AIR);
        }

        public long countSand() {
            return rocks.values().stream().filter(SAND::equals).count();
        }

        public void drop(Point sand) {
            rocks.put(sand, SAND);
        }

        public Cave addFloor() {
            Map<Point, String> newRocks = new HashMap<>(rocks);
            for (int x = left; x <= right; x++)
                newRocks.put(Point.of(x, floor), ROCK);

            return new Cave(newRocks);
        }

        public void display() {
            for (int y = top; y <= floor; y++) {
                System.out.println();
                for (int x = left; x <= right; x++)
                    System.out.print(rocks.getOrDefault(Point.of(x, y), AIR));
            }
            System.out.println();
        }
    }
}
