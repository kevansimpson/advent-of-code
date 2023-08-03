package org.base.advent.code2022;

import org.base.advent.Solution;

import java.util.Comparator;
import java.util.List;

import static org.base.advent.util.Util.splitByBlankLine;

/**
 * <a href="https://adventofcode.com/2022/day/01">Day 01</a>
 */
public class Day01 implements Solution<List<String>> {

    @Override
    public List<String> getInput(){
        return readLines("/2022/input01.txt");
    }

    private final List<List<String>> parsed = splitByBlankLine(getInput());
    private final List<Integer> chunked =
            parsed.stream().map(list -> list.stream().mapToInt(Integer::parseInt).sum()).toList();

    @Override
    public Object solvePart1() {
        return chunked.stream().max(Comparator.naturalOrder()).orElse(1138);
    }

    @Override
    public Object solvePart2() {
        return chunked.stream().sorted(Comparator.reverseOrder()).toList()
                .subList(0, 3).stream().mapToInt(Integer::intValue).sum();
    }
}
