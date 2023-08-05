package org.base.advent.code2022;

import lombok.Getter;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.Solution;

import java.util.List;

/**
 * <a href="https://adventofcode.com/2022/day/04">Day 04</a>
 */
public class Day04 implements Solution<List<String[]>> {
    @Getter
    private final List<String[]> input = readCSVLines("/2022/input04.txt");

    private final List<Pair<Range<Integer>, Range<Integer>>> ranges = input.stream()
            .map(pair -> Pair.of(toRange(pair[0]), toRange(pair[1]))).toList();

    @Override
    public Object solvePart1() {
        return ranges.stream().mapToInt(pair -> {
            if (pair.getLeft().containsRange(pair.getRight()) || pair.getRight().containsRange(pair.getLeft()))
                return 1;
            else
                return 0;
        }).sum();
    }

    @Override
    public Object solvePart2() {
        return ranges.stream().mapToInt(pair -> {
            if (pair.getLeft().isOverlappedBy(pair.getRight()) || pair.getRight().isOverlappedBy(pair.getLeft()))
                return 1;
            else
                return 0;
        }).sum();
    }

    private Range<Integer> toRange(String str) {
        String[] pair = str.split("-");
        return Range.of(Integer.parseInt(pair[0]), Integer.parseInt(pair[1]));
    }
}
