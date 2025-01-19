package org.base.advent.code2016;

import org.base.advent.ParallelSolution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import static java.lang.Integer.parseInt;
import static java.util.Collections.emptyMap;
import static org.apache.commons.lang3.math.NumberUtils.toInt;

/**
 * <a href="https://adventofcode.com/2016/day/12">Day 12</a>
 */
public class Day12 extends ParallelSolution<List<String>> {
    public Day12(ExecutorService pool) {
        super(pool);
    }

    @Override
    public Object solvePart1(List<String> input) {
        return new MonorailComputer(input, emptyMap()).operateAssembunnyCode();
    }

    @Override
    public Object solvePart2(List<String> input) {
        return new MonorailComputer(input, Map.of("c", 1)).operateAssembunnyCode();
    }

    static class MonorailComputer {
        final Map<String, Integer> register = new HashMap<>();
        final String[][] bitsArray;

        public MonorailComputer(List<String> input, Map<String, Integer> initial) {
            List.of("a", "b", "c", "d").forEach(n -> register.put(n, 0));
            register.putAll(initial);
            bitsArray = new String[input.size()][];
            for (int i = 0; i < input.size(); i++)
                bitsArray[i] = input.get(i).split(" ");
        }

        int operateAssembunnyCode() {
            int i = 0;
            while (i < bitsArray.length) {
                int jump = 1;
                String[] bits = bitsArray[i];
                switch (bits[0]) {
                    case "cpy" -> {
                        Integer x = register.get(bits[1]);
                        if (x == null)
                            x = parseInt(bits[1]);
                        register.put(bits[2], x);
                    }
                    case "jnz" -> {
                        Integer x = register.get(bits[1]);
                        if (x == null)
                            x = parseInt(bits[1]);
                        if (x != 0)
                            jump = toInt(bits[2], register.getOrDefault(bits[2], 1));
                    }
                    case "inc" -> register.put(bits[1], register.get(bits[1]) + 1);
                    case "dec" -> register.put(bits[1], register.get(bits[1]) - 1);
                    case "tgl" -> {
                        int tix = register.get(bits[1]);
                        String[] toToggle = bitsArray[(i + tix) % bitsArray.length];
                        switch (toToggle[0]) {
                            case "jnz" -> toToggle[0] = "cpy";
                            case "cpy" -> toToggle[0] = "jnz";
                            case "inc" -> toToggle[0] = "dec";
                            default -> toToggle[0] = "inc";
                        }
                    }
                }
                i += jump;
            }

            return register.get("a");
        }
    }
}