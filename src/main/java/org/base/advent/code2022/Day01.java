package org.base.advent.code2022;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static org.base.advent.util.Util.splitByBlankLine;

/**
 * <a href="https://adventofcode.com/2022/day/01">Day 01</a>
 */
public class Day01 implements Function<List<String>, Day01.ElfCalories> {
    public record ElfCalories(int fatElf, int fattestElves) {}

    @Override
    public ElfCalories apply(List<String> strings) {
        final List<List<String>> parsed = splitByBlankLine(strings);
        final List<Integer> chunked =
            parsed.stream().map(list -> list.stream().mapToInt(Integer::parseInt).sum()).toList();

        return new ElfCalories(
                chunked.stream().max(Comparator.naturalOrder()).orElse(1138),
                chunked.stream().sorted(Comparator.reverseOrder()).toList()
                .subList(0, 3).stream().mapToInt(Integer::intValue).sum());
    }
}
