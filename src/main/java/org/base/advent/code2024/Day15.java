package org.base.advent.code2024;

import org.base.advent.Helpers;
import org.base.advent.ParallelSolution;
import org.base.advent.util.Point;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static java.util.Collections.emptyMap;
import static org.base.advent.util.Util.splitByBlankLine;

/**
 * <a href="https://adventofcode.com/2024/day/15">Day 15</a>
 */
public class Day15 extends ParallelSolution<List<String>> implements Helpers {
    public Day15(ExecutorService pool) {
        super(pool);
    }

    @Override
    public Object solvePart1(List<String> input) {
        setDebug(true);
        return solve(input, StandardWarehouse::new);
    }

    @Override
    public Object solvePart2(List<String> input) {
        return solve(input, WideWarehouse::new);
    }

    Object solve(List<String> input, Function<List<String>, Warehouse> factory) {
        List<List<String>> data = splitByBlankLine(input);
        Warehouse warehouse = factory.apply(data.get(0));
        warehouse.display();
        String movements = String.join("", data.get(1));
        for (char dir : movements.toCharArray())
            warehouse.moveGuard(dir);
        if (debug())
            warehouse.display();

        return warehouse.sumGps();
    }

    interface Warehouse {
        void moveGuard(char dir);
        void display();
        int sumGps();
    }

    private static abstract class BaseWarehouse implements Warehouse {
        final int width;
        final int height;
        final char[][] grid;
        Point guard;

        BaseWarehouse(int w, int h) {
            width = w;
            height = h;
            grid = new char[h][w];
        }

        boolean move(char dir, Point pos, char moving) {
            char at = get(pos);
            switch (at) {
                case '#' -> {
                    return false;
                }
                case '.' -> {
                    set(pos, moving);
                    return true;
                }
                case 'O', '[', ']' -> {
                    if (move(dir, pos.move(dir), at)) {
                        set(pos, moving);
                        return true;
                    }
                }
            }
            return false;
        }

        public void display() {
            System.out.println();
            for (int r = height - 1; r >= 0; r--)
                System.out.println(Arrays.toString(grid[r]).replaceAll(", ", ""));
            System.out.println();
        }

        int sumGps(char box) {
            int gps = 0;
            for (int r = 0; r < height; r++)
                for (int c = 0; c < width; c++) {
                    if (grid[r][c] == box)
                        gps += 100 * (height - r - 1) + c;
                }
            return gps;
        }

        char get(Point pos) {
            return grid[pos.iy()][pos.ix()];
        }

        void set(Point pos, char at) {
            grid[pos.iy()][pos.ix()] = at;
        }
    }

    private static class StandardWarehouse extends BaseWarehouse {
        public StandardWarehouse(List<String> input) {
            super(input.size(), input.size());
            for (int r = height - 1; r >= 0; r--)
                for (int c = 0; c < width; c++) {
                    char at = input.get(r).charAt(c);
                    grid[height - r - 1][c] = at;
                    if (at == '@')
                        guard = Point.of(c, height - r - 1);
                }
        }

        public void moveGuard(char dir) {
            Point next = guard.move(dir);
            if (move(dir, next, '@')) {
                set(guard, '.');
                guard = next;
            }
            System.out.println("move = "+ dir);
            display();
        }

        public int sumGps() {
            return sumGps('O');
        }
    }

    private static class WideWarehouse extends BaseWarehouse {
        public WideWarehouse(List<String> input) {
            super(input.size() * 2, input.size());
            for (int r = height - 1; r >= 0; r--)
                for (int c = 0; c < height; c++) {
                    char at = input.get(r).charAt(c);
                    int h = height - r - 1, w = 2 * c;
                    switch (at) {
                        case '#','.' -> {
                            grid[h][w] = at;
                            grid[h][w + 1] = at;
                        }
                        case '@' -> {
                            guard = Point.of(w, h);
                            grid[h][w] = at;
                            grid[h][w + 1] = '.';
                        }
                        case 'O' -> {
                            grid[h][w] = '[';
                            grid[h][w + 1] = ']';
                        }
                    }
                }
        }

        public int sumGps() {
            return sumGps('[');
        }

        public void moveGuard(char dir) {
            Point next = guard.move(dir);
            switch (dir) {
                case '<', '>' -> {
                    if (move(dir, next, '@')) {
                        set(guard, '.');
                        guard = next;
                    }
                }
                case '^', 'v' -> {
                    Map<Point, Character> moves = moveVertically(dir, guard, '@');
                    if (!moves.isEmpty()) {
                        moves.forEach(this::set);
                        guard = next;
                    }
                }
            }
        }

        Map<Point, Character> moveVertically(char dir, Point pos, char moving) {
            Point next = pos.move(dir);
            char at = get(next);
            switch (at) {
                case '#' -> {
                    return emptyMap();
                }
                case '.' -> {
                    return Map.of(next, moving, pos, '.');
                }
                case '[' -> {
                    Map<Point, Character> move1 = moveVertically(dir, next, at);
                    Map<Point, Character> move2 = moveVertically(dir, next.right(1), ']');
                    if (!move1.isEmpty() && !move2.isEmpty()) {
                        Map<Point, Character> total = merge(move1, move2);
                        total.put(next, moving);
                        total.put(pos, '.');
                        return total;
                    }
                }
                case ']' -> {
                    Map<Point, Character> move1 = moveVertically(dir, next.left(1), '[');
                    Map<Point, Character> move2 = moveVertically(dir, next, at);
                    if (!move1.isEmpty() && !move2.isEmpty()) {
                        Map<Point, Character> total = merge(move1, move2);
                        total.put(next, moving);
                        total.put(pos, '.');
                        return total;
                    }
                }
            }
            return emptyMap();
        }

        Map<Point, Character> merge(Map<Point, Character> m1, Map<Point, Character> m2) {
            Map<Point, Character> total = new HashMap<>(m1);
            for (Map.Entry<Point, Character> e : m2.entrySet())
                if (!total.containsKey(e.getKey()) || e.getValue() != '.')
                    total.put(e.getKey(), e.getValue());

            return total;
        }
    }
}

