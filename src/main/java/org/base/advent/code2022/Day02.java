package org.base.advent.code2022;

import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.Solution;

import java.util.List;

/**
 * <a href="https://adventofcode.com/2022/day/02">Day 02</a>
 */
public class Day02 implements Solution<List<String>> {

    private final List<Pair<Integer, Integer>> pairs =
            getInput().stream().map(Day02::score).toList();

    @Override
    public List<String> getInput(){
        return readLines("/2022/input02.txt");
    }

    @Override
    public Object solvePart1() {
        return pairs.stream().mapToInt(Pair::getLeft).sum();
    }

    @Override
    public Object solvePart2() {
        return pairs.stream().mapToInt(Pair::getRight).sum();
    }

    private static Pair<Integer, Integer> score(String key) {
        return switch (key) {
            case "A X" -> Pair.of(4, 3);
            case "B X" -> Pair.of(1, 1);
            case "C X" -> Pair.of(7, 2);
            case "A Y" -> Pair.of(8, 4);
            case "B Y" -> Pair.of(5, 5);
            case "C Y" -> Pair.of(2, 6);
            case "A Z" -> Pair.of(3, 8);
            case "B Z" -> Pair.of(9, 9);
            case "C Z" -> Pair.of(6, 7);
            default -> throw new IllegalStateException("Unexpected value: " + key);
        };
    }
}
