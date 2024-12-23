package org.base.advent.code2024;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.TimeSaver;
import org.base.advent.util.Node;
import org.base.advent.util.Point;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.base.advent.util.Node.createRootNode;
import static org.base.advent.util.Point.inGrid;

/**
 * <a href="https://adventofcode.com/2024/day/18">Day 18</a>
 */
@AllArgsConstructor
public class Day18 implements Function<List<String>, Pair<Long, String>>, TimeSaver {
    private final int width;
    private final int height;
    private final int firstFallCount;

    @Override
    public Pair<Long, String> apply(List<String> input) {
        LinkedList<Point> unsafe = new LinkedList<>();
        for (String coord : input) {
            String[] arr = coord.split(",");
            unsafe.add(Point.of(Integer.parseInt(arr[1]), Integer.parseInt(arr[0])));
        }
        boolean[][] memory = corruptMemory(unsafe);
        long part1 = findPath(memory);
        Point firstBlocker;
        do {
            firstBlocker = unsafe.removeFirst();
            memory[firstBlocker.iy()][firstBlocker.ix()] = true;
        } while (findPath(memory) > 0L);

        if (isFullSolve())
            displayMemory(memory);

        return Pair.of(part1, String.format("%d,%d", firstBlocker.iy(), firstBlocker.ix()));
    }

    long findPath(boolean[][] memory) {
        boolean found = false;
        Map<Point, Long> visited = new HashMap<>();
        final Point target = Point.of(width - 1, height - 1);
        long minSteps = Long.MAX_VALUE;
        LinkedList<Node<Point>> paths = new LinkedList<>();
        paths.add(createRootNode(Point.ORIGIN));

        while (!paths.isEmpty()) {
            Node<Point> node = paths.removeFirst();
            Point pos = node.getData();
            if (!visited.containsKey(pos)) {
                visited.put(pos, node.getDepth());

                if (pos.equals(target)) {
                    found = true;
                    minSteps = node.getDepth();
                }
                else {
                    for (Point next : pos.cardinal()) {
                        if (inGrid(next, width, height)
                                && !visited.containsKey(next)
                                && !memory[next.iy()][next.ix()])
                            paths.add(node.addChild(next));
                    }
                }
            }

        }

        return (found) ? minSteps - 1L : -1L;
    }

    void displayMemory(boolean[][] memory) {
        for (int y = 0; y < height; y++) {
            System.out.println();
            for (int x = 0; x < width; x++)
                System.out.print(memory[y][x] ? "#" : " ");
        }
        System.out.println();
    }

    boolean[][] corruptMemory(LinkedList<Point> unsafe) {
        boolean[][] memory = new boolean[height][width];
        for (int i = 0; i < firstFallCount; i++) {
            Point corrupted = unsafe.removeFirst();
            memory[corrupted.iy()][corrupted.ix()] = true;
        }
        return memory;
    }
}

