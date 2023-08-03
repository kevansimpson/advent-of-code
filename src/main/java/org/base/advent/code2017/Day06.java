package org.base.advent.code2017;

import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * <a href="https://adventofcode.com/2017/day/06">Day 06</a>
 */
public class Day06 implements Solution<List<Integer>> {

    @Override
    public List<Integer> getInput(){
        return Stream.of(readInput("/2017/input06.txt").split("\\s")).map(Integer::parseInt).collect(Collectors.toList());
    }

    @Override
    public Object solvePart1() {
        return distributionsUntilDuplicate(getInput()).getLeft();
    }

    @Override
    public Object solvePart2() {
        return distributionsUntilDuplicate(distributionsUntilDuplicate(getInput()).getRight()).getLeft();
    }

    // returns infinite loop count and first duplicate distribution
    public Pair<Integer, List<Integer>> distributionsUntilDuplicate(List<Integer> input) {
        int count = 0;
        final Set<String> distributions = new TreeSet<>();

        while (!distributions.contains(input.toString())) {
            distributions.add(input.toString());
            input = distributeBlocks(input);
            ++count;
        }

        return Pair.of(count, input);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
	public List<Integer> distributeBlocks(final List<Integer> input) {
        final int high = input.stream().max(Integer::compare).get();
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
