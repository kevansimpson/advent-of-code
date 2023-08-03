package org.base.advent.code2016;

import org.base.advent.Solution;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <a href="https://adventofcode.com/2016/day/03">Day 03</a>
 */
public class Day03 implements Solution<List<String>> {

    @Override
    public List<String> getInput(){
        return readLines("/2016/input03.txt");
    }

    @Override
    public Object solvePart1() {
        return countTrianglesByRow(getInput());
    }

    @Override
    public Object solvePart2() {
        return countTrianglesByColumn(getInput());
    }

    public long countTrianglesByColumn(final List<String> input) {
        final List<int[]> rows = toIntArray(input).collect(Collectors.toList());
        long valid = 0;
        for (int i = 0, max = input.size(); i < max; i += 3) {
            final int[] row1 = rows.get(i), row2 = rows.get(i + 1), row3 = rows.get(i + 2);
            valid += validTriangle(row1[0], row2[0], row3[0]) ? 1 : 0;
            valid += validTriangle(row1[1], row2[1], row3[1]) ? 1 : 0;
            valid += validTriangle(row1[2], row2[2], row3[2]) ? 1 : 0;
        }

        return valid;
    }

    public long countTrianglesByRow(final List<String> input) {
        return toIntArray(input).filter(this::validTriangle).count();
    }

    Stream<int[]> toIntArray(final List<String> input) {
        return input.stream()
                .map(String::trim)
                .map(str -> Arrays.asList(str.split("\\s+")))
                .map(lst -> lst.stream().mapToInt(Integer::parseInt).toArray());
    }

    boolean validTriangle(int... sides) {
        Arrays.sort(sides);
        return sides.length == 3 && ((sides[0] + sides[1]) > sides[2]);
    }
}
