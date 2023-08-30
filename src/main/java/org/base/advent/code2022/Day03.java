package org.base.advent.code2022;

import org.apache.commons.collections4.ListUtils;
import org.base.advent.Solution;

import java.util.List;
import java.util.Set;

import static org.apache.commons.collections4.SetUtils.intersection;
import static org.base.advent.util.Text.stringToSet;

/**
 * <a href="https://adventofcode.com/2022/day/03">Day 03</a>
 */
public class Day03 implements Solution<List<String>> {
    @Override
    public Object solvePart1(final List<String> input) {
        return sumPriorities(input);
    }

    @Override
    public Object solvePart2(final List<String> input) {
        return sumThreeElves(input);
    }

    int sumPriorities(final List<String> items) {
        return items.stream().map(str -> {
            int ix = str.length() / 2;
            Set<Character> set1 = stringToSet(str.substring(0, ix));
            Set<Character> set2 = stringToSet(str.substring(ix));

            return intersection(set1, set2).stream().mapToInt(this::priority).sum();

        }).mapToInt(Integer::intValue).sum();
    }

    int sumThreeElves(final List<String> items) {
        List<List<String>> chunks = ListUtils.partition(items, 3);
        return chunks.stream().map(chunk -> {
            Set<Character> set1 = stringToSet(chunk.get(0));
            Set<Character> set2 = stringToSet(chunk.get(1));
            Set<Character> set3 = stringToSet(chunk.get(2));

            return intersection(intersection(set1, set2), set3).stream().mapToInt(this::priority).sum();

        }).mapToInt(Integer::intValue).sum();
    }

    private int priority(char ch) {
        if (Character.isLowerCase(ch))
            return ch - 96;
        else
            return ch - 38;
    }
}
