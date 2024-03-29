package org.base.advent.code2015;

import org.base.advent.Solution;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <a href="https://adventofcode.com/2015/day/05">Day 05</a>
 */
public class Day05 implements Solution<List<String>> {
    private static final Pattern VOWELS = Pattern.compile("[aeiou]", Pattern.DOTALL);
    private static final Pattern DOUBLE_LETTERS = Pattern.compile("([a-z])\\1+", Pattern.DOTALL);
    private static final Pattern BAD_STRINGS = Pattern.compile("(ab|cd|pq|xy)", Pattern.DOTALL);
    private static final Pattern NON_OVERLAPPING_LETTER_PAIRS = Pattern.compile("([a-z]{2}).*\\1+", Pattern.DOTALL);
    private static final Pattern SANDWICH_LETTERS = Pattern.compile("([a-z]).\\1+", Pattern.DOTALL);

    @Override
    public Object solvePart1(final List<String> input) {
        return countNiceStrings(input);
    }

    @Override
    public Object solvePart2(final List<String> input) {
        return countNicerStrings(input);
    }

    int countNiceStrings(final List<String> input) {
        int matches = 0;
        for (final String str : input) {
            if (countMatches(str, VOWELS) >= 3)
                if (countMatches(str, DOUBLE_LETTERS) >= 1)
                    if (countMatches(str, BAD_STRINGS) <= 0)
                        ++matches;
        }

        return matches;
    }

    int countNicerStrings(final List<String> input) {
        int matches = 0;
        for (final String str : input) {
            if (countMatches(str, NON_OVERLAPPING_LETTER_PAIRS) >= 1)
                if (countMatches(str, SANDWICH_LETTERS) >= 1)
                    ++matches;
        }

        return matches;
    }
    
    int countMatches(final String input, final Pattern pattern) {
        int count = 0;
        final Matcher matcher = pattern.matcher(input);
        
        while (matcher.find()) {
            count++;
        }

        return count;
    }
}
