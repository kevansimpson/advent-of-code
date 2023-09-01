package org.base.advent.code2016;

import org.base.advent.Solution;

import java.util.Arrays;

/**
 * <a href="https://adventofcode.com/2016/day/9">Day 9</a>
 */
public class Day09 implements Solution<String> {
    @Override
    public Object solvePart1(String input) {
        return decompressV1(input);
    }

    @Override
    public Object solvePart2(String input) {
        return decompressV2(input);
    }

    long decompressV1(String input) {
        long length = 0L;
        int start = 0, index = input.indexOf("(");
        while (index >= 0) {
            length += index - start;
            int close = input.indexOf(")", index);
            long[] marker = Arrays.stream(input.substring(index + 1, close).split("x"))
                    .mapToLong(Long::parseLong).toArray();
            length += marker[0] * marker[1];
            start = close + (int) marker[0] + 1;
            index = input.indexOf("(", start);
        }

        return length + input.substring(start).length();
    }

    long decompressV2(String input) {
        long length = 0L;
        int start = 0, index = input.indexOf("(");
        while (index >= 0) {
            length += index - start;
            int close = input.indexOf(")", index);
            long[] marker = Arrays.stream(input.substring(index + 1, close).split("x"))
                    .mapToLong(Long::parseLong).toArray();
            start = close + 1;
            String repeated = input.substring(start, start + (int) marker[0]);
            length += marker[1] * (repeated.contains("(") ? decompressV2(repeated) : marker[0]);
            start += marker[0];
            index = input.indexOf("(", start);
        }

        return length + input.substring(start).length();
    }
}
