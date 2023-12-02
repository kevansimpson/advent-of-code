package org.base.advent.code2023;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.base.advent.util.Text.findAll;
import static org.base.advent.util.Text.stringsToInts;

/**
 * <a href="https://adventofcode.com/2023/day/1">Day 1</a>
 */
public class Day01 implements Function<List<String>, Day01.DocSum> {
    public record DocSum(int sum1, int sum2) {}

    private static final Pattern digitPattern = Pattern.compile("\\d");
    private static final Pattern writtenPattern = Pattern.compile(
            "(one|two|three|four|five|six|seven|eight|nine|[-\\d])");
    private static final Map<String, Integer> digitMap = Map.of(
            "one", 1, "two", 2, "three", 3,
            "four", 4, "five", 5, "six", 6,
            "seven", 7, "eight", 8, "nine", 9);

    @Override
    public DocSum apply(List<String> input) {
        int sum1 = 0, sum2 = 0;
        for (String str: input) {
            sum1 += combine(stringsToInts(findAll(digitPattern, str)));
            sum2 += combine(mapStringsToInts(extractAllDigits(str)));
        }
        return new DocSum(sum1, sum2);
    }

    private int combine(int[] digits) {
        return Integer.parseInt(String.format("%d%d", digits[0], digits[digits.length - 1]));
    }

    private List<String> extractAllDigits(String str) {
        if (str.isBlank())
            return Collections.emptyList();
        else {
            List<String> ints = new ArrayList<>();
            Matcher m = writtenPattern.matcher(str);
            if (m.find(0))
                ints.add(m.group(1));
            ints.addAll(extractAllDigits(str.substring(1)));
            return ints;
        }
    }

    private int[] mapStringsToInts(List<String> strings) {
        return strings.stream().mapToInt(s -> digitMap.containsKey(s) ? digitMap.get(s) : Integer.parseInt(s)).toArray();
    }
}