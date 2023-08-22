package org.base.advent.code2017;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * <a href="https://adventofcode.com/2017/day/06">Day 06</a>
 */
public class Day06 implements Function<String, Day06.CycleCount> {
    public record CycleCount(int redistribution, int infinite) {}

    @Override
    public CycleCount apply(String input) {
        List<Integer> integers = Stream.of(input.split("\\s")).map(Integer::parseInt).toList();
        Pair<Integer, List<Integer>> part1 = distributionsUntilDuplicate(integers);
        int redistribution = part1.getLeft();
        return new CycleCount(redistribution, distributionsUntilDuplicate(part1.getRight()).getLeft());
    }

    // returns infinite loop count and first duplicate distribution
    Pair<Integer, List<Integer>> distributionsUntilDuplicate(List<Integer> input) {
        int count = 0;
        final Set<String> distributions = new TreeSet<>();

        while (!distributions.contains(input.toString())) {
            distributions.add(input.toString());
            input = distributeBlocks(input);
            ++count;
        }

        return Pair.of(count, input);
    }

    public List<Integer> distributeBlocks(final List<Integer> input) {
        final int high = input.stream().max(Integer::compare).orElseThrow();
        int index = input.indexOf(high);

        final List<Integer> output = new ArrayList<>(input);
        output.set(index, 0);
        for (int i = high; i > 0; i--) {
            ++index;
            if (index >= input.size()) index = 0;

            output.set(index, 1 + output.get(index));
        }

        return output;
    }
}
