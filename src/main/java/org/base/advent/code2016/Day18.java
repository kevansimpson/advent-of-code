package org.base.advent.code2016;

import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.countMatches;

/**
 * <a href="https://adventofcode.com/2016/day/18">Day 18</a>
 */
public class Day18 implements Function<String, Pair<Integer, Integer>> {
    @Override
    public Pair<Integer, Integer> apply(String input) {
        final int length = input.length();
        String row = padSafe(input);
        int safe = countMatches(input, ".");

        for (int r = 1; r < 40; r++) {
            row = padSafe(nextRow(row, length));
            safe += countMatches(row, ".") - 2;
        }
        int at40rows = safe;
        for (int r = 40; r < 400000; r++) {
            row = padSafe(nextRow(row, length));
            safe += countMatches(row, ".") - 2;
        }

        return Pair.of(at40rows, safe);
    }

    String nextRow(String row, int length) {
        StringBuilder next = new StringBuilder();
        for (int i = 1; i <= length; i++) {
            if (row.charAt(i - 1) == row.charAt(i + 1)) {
                next.append(".");
            }
            else
                next.append("^");
        }

        return next.toString();
    }

    String padSafe(String row) {
        return String.format(".%s.", row);
    }
}