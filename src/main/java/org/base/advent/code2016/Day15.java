package org.base.advent.code2016;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.base.advent.util.Text.extractInt;

/**
 * <a href="https://adventofcode.com/2016/day/15">Day 15</a>
 */
public class Day15 implements Function<List<String>, Pair<Integer, Integer>> {
    record Disc(int id, int positions, int time, int atZero) {
        public int discPosition(int currentTime) {
            return (atZero + currentTime) % positions;
        }
    }

    @Override
    public Pair<Integer, Integer> apply(List<String> input) {
        List<Disc> discs = input.stream().map(this::makeDisc).toList();
        List<Disc> extra = new ArrayList<>(discs);
        extra.add(new Disc(discs.size(), 11, 0, 0));
        try (ExecutorService pool = Executors.newFixedThreadPool(2)) {
            CompletableFuture<Integer> f1 = supplyAsync(() -> dropCapsules(discs), pool);
            CompletableFuture<Integer> f2 = supplyAsync(() -> dropCapsules(extra), pool);
            return Pair.of(f1.get(), f2.get());
        }
        catch (Exception ex) {
            throw new RuntimeException("Day15, 2016", ex);
        }
    }

    int dropCapsules(List<Disc> discs) {
        int target = discs.size();
        Map<Integer, Integer> capsules = new LinkedHashMap<>();

        for (int time = 0; time < 4000000; time++) {
            int[] currentPositions = new int[target];
            for (int i = 0; i < target; i++)
                currentPositions[i] = discs.get(i).discPosition(time);

            Set<Integer> keys = new LinkedHashSet<>(capsules.keySet());
            for (int capsule : keys) {
                int d = capsules.get(capsule);
                if (currentPositions[d] == 0) {
                    int falls = d + 1;
                    if (falls == target)
                        return capsule;
                    else
                        capsules.put(capsule, falls);
                }
                else
                    capsules.remove(capsule);
            }
            if (currentPositions[0] == 0)
                capsules.put(time - 1, 1);
        }

        return -1;
    }

    Disc makeDisc(String input) {
        int[] nums = extractInt(input);
        return new Disc(nums[0], nums[1], nums[2], nums[3]);
    }
}