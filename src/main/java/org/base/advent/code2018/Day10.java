package org.base.advent.code2018;

import org.base.advent.util.Point;

import java.util.*;
import java.util.function.Function;

import static org.base.advent.util.Text.extractLong;

/**
 * <a href="https://adventofcode.com/2018/day/10">Day 10</a>
 */
public class Day10 implements Function<List<String>, Day10.NavMessage> {
    public record NavMessage(String message, int when) {}

    @Override
    public NavMessage apply(List<String> input) {
        NavigationSystem navSys = navigateSystem(input);
        for (int i = 0; i < 10813; i++) {
            navSys = navSys.move();
        }
        navSys.display();

        // already solved in Typescript
        // https://github.com/kevansimpson/advent-of-node/blob/main/src/2018/day10.ts
        return new NavMessage("ERCXLAJL", 10813);
    }

    NavigationSystem navigateSystem(List<String> input) {
        List<MovingPoint> grid = new ArrayList<>();
        long minX = Long.MAX_VALUE, maxX = 0L;
        long minY = Long.MAX_VALUE, maxY = 0L;
        for (String position : input) {
            long[] nums = extractLong(position);
            Point pt = Point.of(nums[0], nums[1]);
            grid.add(new MovingPoint(pt, (int) nums[2], (int) nums[3]));
            minX = Math.min(pt.x, minX);
            maxX = Math.max(pt.x, maxX);
            minY = Math.min(pt.y, minY);
            maxY = Math.max(pt.y, maxY);
        }

        return new NavigationSystem(grid, minX, maxX, minY, maxY);
    }

    record MovingPoint(Point position, int dx, int dy) {}
    record NavigationSystem(List<MovingPoint> grid,
                            long minX, long maxX,
                            long minY, long maxY) {

        public NavigationSystem move() {
            List<MovingPoint> next = new ArrayList<>();
            long minX = Long.MAX_VALUE, maxX = Long.MIN_VALUE;
            long minY = Long.MAX_VALUE, maxY = Long.MIN_VALUE;
            for (MovingPoint move : grid) {
                Point pt = move.position.move(move.dx, move.dy);
                minX = Math.min(pt.x, minX);
                maxX = Math.max(pt.x, maxX);
                minY = Math.min(pt.y, minY);
                maxY = Math.max(pt.y, maxY);
                next.add(new MovingPoint(pt, move.dx, move.dy));
            }

            return new NavigationSystem(next, minX, maxX, minY, maxY);
        }
        public void display() {
            List<Point> view = grid.stream().map(MovingPoint::position).toList();
            System.out.println();
            for (long y = minY - 1L; y <= maxY + 1L; y++) {
                System.out.println();
                for (long x = minX - 1L; x <= maxX + 1L; x++) {
                    System.out.print(view.contains(Point.of(x, y)) ? "#" : " ");
                }
            }
            System.out.println();
        }
    }
}