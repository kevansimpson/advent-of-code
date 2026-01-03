package org.base.advent.code2025;

import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.ParallelSolution;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

/**
 * <a href="https://adventofcode.com/2025/day/11">Day 11</a>
 */
public class Day11 extends ParallelSolution<List<String>> {
    record State(String step, boolean dac, boolean fft) {
        boolean isValid() {
            return dac && fft;
        }
    }

    public Day11(ExecutorService pool) {
        super(pool);
    }

    @Override
    public Object solvePart1(List<String> input) {
        return countPaths1(parseInput(input));
    }

    @Override
    public Object solvePart2(List<String> input) {
        Map<String, String[]> outputs = parseInput(input);
        // svr to out visit both dac and fft
        return countPaths2(outputs);
    }

    int countPaths1(Map<String, String[]> outputs) {
        Set<String> validPaths = new HashSet<>(), visited = new HashSet<>();
        LinkedList<List<String>> paths = new LinkedList<>();
        paths.add(List.of("you"));

        while (!paths.isEmpty()) {
            List<String> current = paths.removeFirst();
            String last = current.get(current.size() - 1), key = current.toString();
            if ("out".equals(last))
                validPaths.add(key);
            else if (!visited.contains(key)) {
                visited.add(key);
                if (outputs.containsKey(last))
                    for (String out : outputs.get(last)) {
                        List<String> newPath = new ArrayList<>(current);
                        newPath.add(out);
                        paths.add(newPath);
                    }
            }
        }
        return validPaths.size();
    }

    long countPaths2(Map<String, String[]> outputs) {
        Map<State, Long> pathCounts = new HashMap<>();
        return dfs(new State("svr", false, false), outputs, pathCounts, new HashSet<>());
    }

    long dfs(State current, Map<String, String[]> outputs, Map<State, Long> pathCounts, Set<State> visited) {
        if ("out".equals(current.step))
            return current.isValid() ? 1L : 0L;

        if (pathCounts.containsKey(current))
            return pathCounts.get(current);

        if (visited.contains(current))
            return 0L;

        visited.add(current);
        long totalPaths = 0L;
        if (outputs.containsKey(current.step))
            for (String out : outputs.get(current.step)) {
                State next = new State(
                        out,
                        current.dac || "dac".equals(out),
                        current.fft || "fft".equals(out));
                totalPaths += dfs(next, outputs, pathCounts, visited);
            }

        visited.remove(current);
        pathCounts.put(current, totalPaths);
        return totalPaths;
    }

    Map<String, String[]> parseInput(List<String> input) {
        Map<String, String[]> result = new HashMap<>();
        for (String line : input) {
            String[] parts = line.split(": ");
            result.put(parts[0], parts[1].split(" "));
        }
        return result;
    }
}

