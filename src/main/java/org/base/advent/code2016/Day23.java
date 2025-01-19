package org.base.advent.code2016;

import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.code2016.Day12.MonorailComputer;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.base.advent.util.Util.factorial;

/**
 * <a href="https://adventofcode.com/2016/day/23">Day 23</a>
 */
public class Day23 implements Function<List<String>, Pair<Integer, Integer>> {
    @Override
    public Pair<Integer, Integer> apply(List<String> input) {
        int part1 = new MonorailComputer(input, Map.of("a", 7)).operateAssembunnyCode();
        // reddit hints pointed to factorials; never done assembly
        int part2 = part1 + factorial(12).intValue() - factorial(7).intValue();
        return Pair.of(part1, part2);
    }
}