package org.base.advent.code2024;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.base.advent.util.Util.reverse;

/**
 * <a href="https://adventofcode.com/2024/day/2">Day 2</a>
 */
public class Day02 implements Function<List<String>, Pair<Integer, Integer>> {
    @Override
    public Pair<Integer, Integer> apply(List<String> input) {
        int safe = 0, singleBad = 0;
        for (String line : input) {
            int[] levels = Stream.of(line.split("\\s+")).mapToInt(Integer::parseInt).toArray();
            if (isSafe(levels))
                safe++;
            if (isSingleBad(levels))
                singleBad++;
        }
        return Pair.of(safe, singleBad);
    }

    private boolean isSafe(int[] levels) {
        int[] sorted = Arrays.copyOf(levels, levels.length);
        Arrays.sort(sorted);
        if (levels[0] > levels[1]) {// decreasing
            sorted = reverse(sorted);
        }
        else if (sorted[0] == sorted[1])
            return false;

        if (Arrays.equals(levels, sorted)) {
            for (int i = 0; i < levels.length - 1; i++) {
                int diff = Math.abs(levels[i] - levels[i + 1]);
                if (diff < 1 || diff > 3)
                    return false;
            }
            return true;
        }

        return false;
    }

    private boolean isSingleBad(int[] levels) {
        for (int i = 0; i < levels.length; i++) {
            if (isSafe(remove(levels, i)))
                return true;
        }

        return false;
    }

    private int[] remove(int[] arr, int in) {
        if (arr == null || in < 0 || in >= arr.length) {
            return arr;
        }

        int[] arr2 = new int[arr.length - 1];

        // Copy the elements except the index
        // from original array to the other array
        for (int i = 0, k = 0; i < arr.length; i++) {
            if (i == in)
                continue;

            arr2[k++] = arr[i];
        }

        return arr2;
    }
}

