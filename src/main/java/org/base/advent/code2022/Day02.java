package org.base.advent.code2022;

import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.Solution;

import java.util.List;
import java.util.Map;

/**
 * <a href="https://adventofcode.com/2022/day/02">Day 02</a>
 */
public class Day02 implements Solution<List<String>> {
    @Getter
    private final List<String> input = readLines("/2022/input02.txt");

    private final List<Pair<Integer, Integer>> pairs =
            getInput().stream().map(Day02::score).toList();

    @Override
    public Object solvePart1() {
        return pairs.stream().mapToInt(Pair::getLeft).sum();
    }

    @Override
    public Object solvePart2() {
        return pairs.stream().mapToInt(Pair::getRight).sum();
    }

    private static final Map<String, Pair<Integer, Integer>> SCORE_MAP = Map.of(
            "A X", Pair.of(4, 3), "B X", Pair.of(1, 1), "C X", Pair.of(7, 2),
            "A Y", Pair.of(8, 4), "B Y", Pair.of(5, 5), "C Y", Pair.of(2, 6),
            "A Z", Pair.of(3, 8), "B Z", Pair.of(9, 9), "C Z", Pair.of(6, 7));

    private static Pair<Integer, Integer> score(String key) {
        return SCORE_MAP.get(key);
    }
}
