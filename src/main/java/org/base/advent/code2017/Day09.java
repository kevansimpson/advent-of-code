package org.base.advent.code2017;

import org.apache.commons.lang3.StringUtils;
import org.base.advent.Solution;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <a href="https://adventofcode.com/2017/day/09">Day 09</a>
 */
public class Day09 implements Solution<String> {
    @Override
    public Object solvePart1(final String input) {
        return score(input);
    }

    @Override
    public Object solvePart2(final String input) {
        return countGarbage(input);
    }

    int countGarbage(String input) {
        final Pattern PATTERN = Pattern.compile("<([^>]+)>");
        input = input.replaceAll( "(!.)", "");
        int count = 0;
        int start = 0;
        final Matcher matcher = PATTERN.matcher(input);
        while (matcher.find(start)) {
            count += matcher.group(1).length();
            start = matcher.end() + 1;

            if (start >= input.length()) break;
        }

        return count;
    }

    int score(String input) {
        input = clean(input);
        int level = 0;
        int score = 0;

        for (final char ch : input.toCharArray()) {
            switch (ch) {
                case '{' -> ++level;
                case '}' -> {
                    score += level;
                    --level;
                }
            }
        }

        return score;
    }

    int countGroups(String input) {
        input = clean(input);
        return Math.min(StringUtils.countMatches(input, "{"), StringUtils.countMatches(input, "}"));
    }

    String clean(String input) {
        input = input.replaceAll("(!.)", "");
        input = input.replaceAll("(<[^>]+>)", "");

        return input;
    }
}
