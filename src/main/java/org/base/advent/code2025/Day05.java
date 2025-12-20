package org.base.advent.code2025;

import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.Function;

import static java.lang.Long.parseLong;

/**
 * <a href="https://adventofcode.com/2025/day/5">Day 5</a>
 */
public class Day05 implements Function<List<String>, Pair<Integer, Long>> {
    @Override
    public Pair<Integer, Long> apply(List<String> input) {
        List<Range<Long>> ranges = new ArrayList<>();
        int row = 0;
        while (!input.get(row).isBlank()) {
            String[] raw = input.get(row).split("-");
            ranges.add(Range.of(parseLong(raw[0]), parseLong(raw[1])));
            row++;
        }

        ranges = mergeRanges(ranges);

        int fresh = 0;
        for (int r = row + 1; r < input.size(); r++) {
            long ingredient = parseLong(input.get(r));
            for (Range<Long> range : ranges) {
                if (range.contains(ingredient)) {
                    fresh++;
                    break;
                }
            }
        }

        long total = 0;
        for (Range<Long> range : ranges)
            total += range.getMaximum() - range.getMinimum() + 1;

        return Pair.of(fresh, total);
    }

    static List<Range<Long>> mergeRanges(List<Range<Long>> ranges) {
        if (ranges == null || ranges.isEmpty()) {
            return new ArrayList<>();
        }

        // Sort by minimum value
        List<Range<Long>> sorted = new ArrayList<>(ranges);
        sorted.sort(Comparator.comparing(Range::getMinimum));

        List<Range<Long>> merged = new ArrayList<>();
        Range<Long> current = sorted.get(0);

        for (int i = 1; i < sorted.size(); i++) {
            Range<Long> next = sorted.get(i);

            // Check if ranges overlap or are adjacent
            if (current.isOverlappedBy(next) || current.getMaximum() + 1 == next.getMinimum()) {
                // Merge ranges
                current = Range.of(
                        Math.min(current.getMinimum(), next.getMinimum()),
                        Math.max(current.getMaximum(), next.getMaximum()));
            } else {
                // No overlap, add current and move to next
                merged.add(current);
                current = next;
            }
        }

        merged.add(current);
        return merged;
    }
}

