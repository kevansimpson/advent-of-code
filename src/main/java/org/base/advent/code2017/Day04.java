package org.base.advent.code2017;

import org.base.advent.Solution;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <a href="https://adventofcode.com/2017/day/04">Day 04</a>
 */
public class Day04 implements Solution<List<String>> {
    @Override
    public Object solvePart1(final List<String> input) {
        return countValidPassphrases(input, this::isValid);
    }

    @Override
    public Object solvePart2(final List<String> input) {
        return countValidPassphrases(input, this::hasAnagrams);
    }

    long countValidPassphrases(final List<String> input, final Predicate<String> predicate) {
        return input.parallelStream().filter(predicate).count();
    }

    boolean isValid(final String passphrase) {
        String[] tokens = passphrase.split("\\s");
        return tokens.length == Stream.of(tokens).collect(Collectors.toSet()).size();
    }

    boolean hasAnagrams(final String passphrase) {
        String[] tokens = passphrase.split("\\s");
        return tokens.length == Stream.of(tokens).map(this::sortString).collect(Collectors.toSet()).size();
    }
}
