package org.base.advent.code2017;

import lombok.Getter;
import org.base.advent.Solution;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <a href="https://adventofcode.com/2017/day/04">Day 04</a>
 */
public class Day04 implements Solution<List<String>> {
    @Getter
    private final List<String> input =  readLines("/2017/input04.txt");

    @Override
    public Object solvePart1() {
        return countValidPassphrases(getInput(), this::isValid);
    }

    @Override
    public Object solvePart2() {
        return countValidPassphrases(getInput(), this::hasAnagrams);
    }

    public long countValidPassphrases(List<String> input, Predicate<String> predicate) {
        return input.parallelStream().filter(predicate).count();
    }

    public boolean isValid(String passphrase) {
        String[] tokens = passphrase.split("\\s");
        return tokens.length == Stream.of(tokens).collect(Collectors.toSet()).size();
    }

    public boolean hasAnagrams(String passphrase) {
        String[] tokens = passphrase.split("\\s");
        return tokens.length == Stream.of(tokens).map(this::sortString).collect(Collectors.toSet()).size();
    }

}
