package org.base.advent.code2016;

import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static java.util.Comparator.comparingLong;
import static org.base.advent.util.Util.merge;

/**
 * <a href="https://adventofcode.com/2016/day/20">Day 20</a>
 */
public class Day20 implements Function<List<String>, Pair<Long, Long>> {
    @Override
    public Pair<Long, Long> apply(List<String> input) {
        List<Range<Long>> ranges = input.stream()
                .map(str -> Arrays.stream(str.split("-"))
                        .mapToLong(Long::parseLong).toArray())
                .map(arr -> Range.of(arr[0], arr[1]))
                .sorted(comparingLong(Range::getMinimum))
                .toList();

        long lowest = -1L, unblocked = 0L;
        AtomicReference<Range<Long>> overlap = new AtomicReference<>(Range.of(0L, 0L));
        for (Range<Long> range : ranges) {
            Pair<Range<Long>, Range<Long>> pair = merge(overlap.get(), range);
            if (pair.getRight() != null) {
                unblocked += (pair.getRight().getMinimum() - pair.getLeft().getMaximum() - 1);
                if (lowest < 0L)
                    lowest = pair.getLeft().getMaximum() + 1L;

                overlap.set(pair.getRight());
            }
            else
                overlap.set(pair.getLeft());
        }

        return Pair.of(lowest, unblocked);
    }
}