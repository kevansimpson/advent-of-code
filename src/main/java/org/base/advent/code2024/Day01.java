package org.base.advent.code2024;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.Function;

/**
 * <a href="https://adventofcode.com/2024/day/1">Day 1</a>
 */
public class Day01 implements Function<List<String>, Pair<Integer, Integer>> {
    @Override
    public Pair<Integer, Integer> apply(List<String> input) {
        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();
        for (String line : input) {
            String[] arr = line.split("\\s+");
            left.add(Integer.parseInt(arr[0]));
            right.add(Integer.parseInt(arr[1]));
        }
        Collections.sort(left);
        Collections.sort(right);

        int diff = 0, score = 0;
        Map<Integer, Integer> counts = getCounts(right);
        for (int i = 0; i < left.size(); i++) {
            diff += Math.abs(left.get(i) - right.get(i));
            score += left.get(i) * counts.getOrDefault(left.get(i), 0);
        }

        return Pair.of(diff, score);
    }

    private Map<Integer, Integer> getCounts(List<Integer> right) {
        Map<Integer, Integer> counts = new TreeMap<>();
        for (int val : right) {
            if (counts.containsKey(val))
                counts.put(val, counts.get(val) + 1);
            else
                counts.put(val, 1);
        }

        return counts;
    }
}

