package org.base.advent.code2022;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * <a href="https://adventofcode.com/2022/day/02">Day 02</a>
 */
public class Day02 implements Function<List<String>, Day02.StrategyScores> {
    public record StrategyScores(int round1, int round2) {}

    @Override
    public StrategyScores apply(List<String> input) {
        final List<Pair<Integer, Integer>> pairs = input.stream().map(Day02::score).toList();
        int round1 = 0, round2 = 0;
        for (Pair<Integer, Integer> p : pairs) {
            round1 += p.getLeft();
            round2 += p.getRight();
        }
        return new StrategyScores(round1, round2);
    }

    private static final Map<String, Pair<Integer, Integer>> SCORE_MAP = Map.of(
            "A X", Pair.of(4, 3), "B X", Pair.of(1, 1), "C X", Pair.of(7, 2),
            "A Y", Pair.of(8, 4), "B Y", Pair.of(5, 5), "C Y", Pair.of(2, 6),
            "A Z", Pair.of(3, 8), "B Z", Pair.of(9, 9), "C Z", Pair.of(6, 7));

    private static Pair<Integer, Integer> score(String key) {
        return SCORE_MAP.get(key);
    }
}
