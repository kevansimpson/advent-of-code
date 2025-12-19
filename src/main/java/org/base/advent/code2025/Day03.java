package org.base.advent.code2025;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.Function;

import static java.lang.Long.parseLong;

/**
 * <a href="https://adventofcode.com/2025/day/3">Day 3</a>
 */
public class Day03 implements Function<List<String>, Pair<Long, Long>> {
    @Override
    public Pair<Long, Long> apply(List<String> input) {
        long totalJoltage2 = 0L, totalJoltage12 = 0L;
        for (String battery : input) {
            totalJoltage2 += calculateJoltage(battery, 2);
            totalJoltage12 += calculateJoltage(battery, 12);
        }
        return Pair.of(totalJoltage2, totalJoltage12);
    }

    long calculateJoltage(String str, int len) {
        int pos = 0, strLen = str.length();
        StringBuilder joltage = new StringBuilder();
        while (joltage.length() < len) {
            int end = strLen - (len - (1 + joltage.length()));
            String remaining = str.substring(pos, end);
            String next = highestDigit(remaining);
            joltage.append(next);
            pos += remaining.indexOf(next) + 1;
        }

        return parseLong(joltage.toString());
    }

    public static String highestDigit(String str) {
        int max = 0;
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                int digit = c - '0';
                if (digit > max) {
                    max = digit;
                }
            }
        }
        return String.valueOf(max);
    }
}

