package org.base.advent.code2024;

import java.util.*;
import java.util.function.Function;

/**
 * <a href="https://adventofcode.com/2024/day/1">Day 1</a>
 */
public class Day01 implements Function<List<String>, Day01.DiffScore> {
    public record DiffScore(int diff, int score) {}

    @Override
    public DiffScore apply(List<String> input) {
        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();
        for (String line : input) {
            String[] arr = line.split("\\s+");
            left.add(Integer.parseInt(arr[0]));
            right.add(Integer.parseInt(arr[1]));
        }
        Collections.sort(left);
        Collections.sort(right);

        return new DiffScore(compareLists(left, right), similarityScore(left, right));
    }

    private int compareLists(List<Integer> left, List<Integer> right) {
        int sum = 0;
        for (int i = 0; i < left.size(); i++)
            sum += Math.abs(left.get(i) - right.get(i));

        return sum;
    }

    private int similarityScore(List<Integer> left, List<Integer> right) {
        int sum = 0;
        Map<Integer, Integer> counts = new TreeMap<>();
        for (int val : right) {
            if (counts.containsKey(val))
                counts.put(val, counts.get(val) + 1);
            else
                counts.put(val, 1);
        }

        for (int i = 0; i < left.size(); i++)
            sum += left.get(i) * counts.getOrDefault(left.get(i), 0);

        return sum;
    }
}

