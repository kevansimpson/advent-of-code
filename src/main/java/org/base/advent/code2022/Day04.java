package org.base.advent.code2022;

import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.Function;

/**
 * <a href="https://adventofcode.com/2022/day/04">Day 04</a>
 */
public class Day04 implements Function<List<String[]>, Day04.AssignmentPairs> {
    public record AssignmentPairs(long contains, long overlaps) {}

    @Override
    public AssignmentPairs apply(List<String[]> input) {
        final List<Pair<Range<Integer>, Range<Integer>>> ranges = input.stream()
            .map(pair -> Pair.of(toRange(pair[0]), toRange(pair[1]))).toList();
        return new AssignmentPairs(
                ranges.stream().filter(pair ->
                        pair.getLeft().containsRange(pair.getRight()) ||
                        pair.getRight().containsRange(pair.getLeft())).count(),
                ranges.stream().filter(pair ->
                        pair.getLeft().isOverlappedBy(pair.getRight()) ||
                        pair.getRight().isOverlappedBy(pair.getLeft())).count());
    }

    private Range<Integer> toRange(String str) {
        String[] pair = str.split("-");
        return Range.of(Integer.parseInt(pair[0]), Integer.parseInt(pair[1]));
    }
}
