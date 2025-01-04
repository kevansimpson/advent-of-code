package org.base.advent.code2016;

import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * <a href="https://adventofcode.com/2016/day/12">Day 12</a>
 */
public class Day12 implements Function<List<String>, Pair<Integer, Integer>> {
    @Override
    public Pair<Integer, Integer> apply(List<String> input) {
        try (ExecutorService pool = Executors.newFixedThreadPool(2)) {
            CompletableFuture<Integer> f0 = supplyAsync(() -> operateAssembunnyCode(input, 0), pool);
            CompletableFuture<Integer> f1 = supplyAsync(() -> operateAssembunnyCode(input, 1), pool);
            int a0 = f0.completeOnTimeout(-1, 5, TimeUnit.SECONDS).get();
            int a1 = f1.completeOnTimeout(-1, 5, TimeUnit.SECONDS).get();

            return Pair.of(a0, a1);
        }
        catch (Exception ex) {
            throw new RuntimeException("Day12, 2016", ex);
        }
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