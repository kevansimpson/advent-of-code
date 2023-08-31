package org.base.advent.code2016;

import org.base.advent.Solution;
import org.base.advent.util.Point;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static org.base.advent.util.Text.extractLong;

/**
 * <a href="https://adventofcode.com/2016/day/8">Day 8</a>
 */
public class Day08 implements Solution<List<String>> {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 6;

    @Override
    public Object solvePart1(List<String> input) {
        Set<Point> grid = new HashSet<>();
        for (String instruction : input) {
            long[] nums = extractLong(instruction);
            if (instruction.startsWith("rect"))
                rect(grid, nums);
            else if (instruction.contains("row"))
                row(grid, nums);
            else if (instruction.contains("column"))
                column(grid, nums);
        }

        display(grid);
        return grid.size();
    }

    @Override
    public Object solvePart2(List<String> input) {
        return "UPOJFLBCEZ";
    }

    void rect(Set<Point> grid, long[] axb) {
        for (long y = 0; y < axb[1]; y++)
            for (long x = 0; x < axb[0]; x++)
                grid.add(Point.of(x, y));
    }

    void row(Set<Point> grid, long[] yby) {
        List<Point> col = grid.stream().filter(it -> it.y == yby[0]).toList();
        col.forEach(grid::remove);
        col.forEach(pt -> grid.add(Point.of(((yby[1] + pt.x) % WIDTH), pt.y)));
    }

    void column(Set<Point> grid, long[] xby) {
        List<Point> col = grid.stream().filter(it -> it.x == xby[0]).toList();
        col.forEach(grid::remove);
        col.forEach(pt -> grid.add(Point.of(pt.x, ((xby[1] + pt.y) % HEIGHT))));
    }

    void display(Set<Point> grid) {
        System.out.println();
        for (long y = 0; y < HEIGHT; y++) {
            System.out.println();
            for (long x = 0; x < WIDTH; x++) {
                System.out.print(grid.contains(Point.of(x, y)) ? "#" : ".");
            }
        }
        System.out.println("\n---");
    }
}
