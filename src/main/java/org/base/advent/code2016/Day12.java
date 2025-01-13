package org.base.advent.code2016;

import org.base.advent.ParallelSolution;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * <a href="https://adventofcode.com/2016/day/12">Day 12</a>
 */
public class Day12 extends ParallelSolution<List<String>> {
    public Day12(ExecutorService pool) {
        super(pool);
    }

    @Override
    public Object solvePart1(List<String> input) {
        return operateAssembunnyCode(input, 0);
    }

    @Override
    public Object solvePart2(List<String> input) {
        return operateAssembunnyCode(input, 1);
    }

    int operateAssembunnyCode(List<String> input, int initialC) {
        Map<String, Integer> register = new LinkedHashMap<>(
                Map.of("a", 0, "b", 0, "c", initialC, "d", 0));
        int i = 0;
        while (i < input.size()) {
            int jump = 1;
            String instruction = input.get(i);
            String[] bits = instruction.split(" ");
            switch (bits[0]) {
                case "cpy" -> {
                    Integer x = register.get(bits[1]);
                    if (x == null)
                        x = Integer.parseInt(bits[1]);
                    register.put(bits[2], x);
                }
                case "jnz" -> {
                    Integer x = register.get(bits[1]);
                    if (x == null)
                        x = Integer.parseInt(bits[1]);
                    if (x != 0)
                        jump = Integer.parseInt(bits[2]);
                }
                case "inc" ->
                        register.put(bits[1], register.get(bits[1]) + 1);
                case "dec" ->
                        register.put(bits[1], register.get(bits[1]) - 1);
            }
            i += jump;
        }

        return register.get("a");
    }
}