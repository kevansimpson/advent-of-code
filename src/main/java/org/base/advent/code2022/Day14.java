package org.base.advent.code2022;

import lombok.Getter;
import org.base.advent.Solution;
import org.base.advent.TimeSaver;
import org.base.advent.util.Point;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Math.*;

/**
 * <a href="https://adventofcode.com/2022/day/14">Day 14</a>
 */
public class Day14 implements Solution<List<String>>, TimeSaver {
    private static final String ROCK = "#";
    private static final String SAND = "o";
    private static final String AIR = " ";
    private static final String SRC = "+";
    private static final Point DROP = Point.of(500, 0);

    @Getter
    private final List<String> input = readLines("/2022/input14.txt");
    private final Cave cave1 = buildCave(input.stream().map(this::scanRocks).toList());
    private final Cave cave2 = cave1.addFloor();

    @Override
    public Object solvePart1() {
        return pourSand(cave1);
    }

    @Override
    public Object solvePart2() {
        return pourSand(cave2);
    }

    long pourSand(Cave cave) {
        Point sand = dropSand(cave, DROP);
        while (sand != null) {
            cave.drop(sand);
            sand = (DROP.equals(sand)) ? null : dropSand(cave, DROP);
        }
        if (isFullSolve())
            cave.display();

        return cave.countSand();
    }

    Point dropSand(Cave cave, Point drop) {
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

    List<Point> rockPath(Point start, Point end) {
        int dx = abs(start.x - end.x);
        if (dx == 0) {
            return IntStream.rangeClosed(min(start.y, end.y), max(start.y, end.y))
                    .mapToObj(y -> Point.of(start.x, y)).toList();
        }
        int dy = abs(start.y - end.y);
        return IntStream.rangeClosed(min(start.x, end.x), max(start.x, end.x))
                .mapToObj(x -> Point.of(x, start.y)).toList();
    }

    private Cave buildCave(List<List<Point>> paths) {
        Map<Point, String> rocks = new HashMap<>(Map.of(DROP, SRC));
        paths.forEach(path -> {
            for (int i = 0, max = path.size() - 1; i < max; i++)
                for (Point pt : rockPath(path.get(i), path.get(i + 1)))
                    rocks.put(pt, ROCK);
        });

        return new Cave(rocks);
    }

    private List<Point> scanRocks(String str) {
        return Stream.of(str.split(" -> ")).map(Point::point).toList();
    }

    private class Cave {
        private final Map<Point, String> rocks;
        private final int leftRock;
        private final int rightRock;
        private final int width;
        private final int left;
        private final int right;
        private final int top;
        private final int bottom;
        private final int floor;

        public Cave(Map<Point, String> someRocks) {
            rocks = someRocks;
            leftRock = rocks.keySet().parallelStream().mapToInt(it -> it.x).min().orElseThrow();
            rightRock = rocks.keySet().parallelStream().mapToInt(it -> it.x).max().orElseThrow();
            width = (rightRock - leftRock) * 3;
            left = leftRock - width;
            right = rightRock + width;
            top = min(0, rocks.keySet().parallelStream().mapToInt(it -> it.y).min().orElseThrow());
            bottom = rocks.keySet().parallelStream().mapToInt(it -> it.y).max().orElseThrow();
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
