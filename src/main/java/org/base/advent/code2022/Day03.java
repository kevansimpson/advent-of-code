package org.base.advent.code2022;

import lombok.Getter;
import org.base.advent.Solution;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.SetUtils.intersection;
import static org.base.advent.util.Util.stringToSet;

/**
 * <a href="https://adventofcode.com/2022/day/03">Day 03</a>
 */
public class Day03 implements Solution<List<String>> {
    @Getter
    private final List<String> input = readLines("/2022/input03.txt");

    @Override
    public Object solvePart1() {
        return sumPriorities(getInput());
    }

    @Override
    public Object solvePart2() {
        return sumThreeElves(getInput()); //2522;
    }

    public int sumPriorities(List<String> items) {
        return items.stream().map(str -> {
            int ix = str.length() / 2;
            Set<Character> set1 = stringToSet(str.substring(0, ix));
            Set<Character> set2 = stringToSet(str.substring(ix));

            return intersection(set1, set2).stream().mapToInt(this::priority).sum();

        }).mapToInt(Integer::intValue).sum();
    }

    public int sumThreeElves(List<String> items) {
        // https://e.printstacktrace.blog/divide-a-list-to-lists-of-n-size-in-Java-8/
        AtomicInteger counter = new AtomicInteger();
        Collection<List<String>> chunks = items.stream()
                .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / 3)).values();
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
