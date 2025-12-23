package org.base.advent.code2025;

import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.ParallelSolution;
import org.base.advent.util.Node;
import org.base.advent.util.Util;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

/**
 * <a href="https://adventofcode.com/2025/day/7">Day 7</a>
 */
public class Day07 extends ParallelSolution<List<String>> {
    public Day07(ExecutorService pool) {
        super(pool);
    }

    @Override
    public Object solvePart1(List<String> input) {
        Set<Integer> beams = new HashSet<>();
        beams.add(input.get(0).indexOf('S'));
        int split = 0;

        for (int i = 1; i < input.size(); i++) {
            Set<Integer> next = new HashSet<>();
            String row = input.get(i);
            for (int b : beams) {
                if (row.charAt(b) == '^') {
                    next.add(b - 1);
                    next.add(b + 1);
                    split++;
                }
                else
                    next.add(b);
            }
            beams = next;
        }
        return split;
    }

    record Beam(int index, int row, List<String> input,
                AtomicLong count, ExecutorService pool) implements Runnable {
        @Override
        public void run() {
            if (row >= input.size())
                count.incrementAndGet();
            else {
                String line = input.get(row);
                if (line.charAt(index) == '^') {
                    Future<?> future1 =
                            pool.submit(new Beam(index - 1, row + 1, input, count, pool));
                    Util.safeGet(future1);
                    Future<?> future2 =
                            pool.submit(new Beam(index + 1, row + 1, input, count, pool));
                    Util.safeGet(future2);
                }
                else {
                    Future<?> future =
                            pool.submit(new Beam(index, row + 1, input, count, pool));
                    Util.safeGet(future);
                }
            }
        }
    }

    @Override
    public Object solvePart2(List<String> input) {
        int rows = input.size(), cols = input.get(0).length();
        long[][] beams = new long[rows][cols];
        beams[0][input.get(0).indexOf('S')] = 1L;

        for (int row = 1; row < rows; row++) {
            String line = input.get(row);
            for (int col = 0; col < cols; col++) {
                long count = beams[row - 1][col];
                if (line.charAt(col) == '^') {
                    beams[row][col - 1] += count;
                    beams[row][col + 1] += count;
                }
                else
                    beams[row][col] += count;
            }
        }
        return Arrays.stream(beams[rows - 1]).sum();
    }

    void beam(int index, int row, List<String> input, AtomicLong count) {
        if (row >= input.size())
            count.incrementAndGet();
        else {
            String line = input.get(row);
            if (line.charAt(index) == '^') {
                beam(index - 1, row + 1, input, count);
                beam(index + 1, row + 1, input, count);
            }
            else
                beam(index, row + 1, input, count);
        }

    }
}
