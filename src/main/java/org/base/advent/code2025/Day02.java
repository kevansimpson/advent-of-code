package org.base.advent.code2025;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

/**
 * <a href="https://adventofcode.com/2025/day/2">Day 2</a>
 */
public class Day02 implements Function<String, Pair<Long, Long>> {
    private static final Pattern DUPLICATES = Pattern.compile("^(\\d+)\\1+$");
    @Override
    public Pair<Long, Long> apply(String input) {
        long invalid1 = 0, invalid2 = 0;
        String[] raw = input.split(",");
        for (String r : raw) {
            String[] range = r.split("-");
            long min = parseLong(range[0]), max = parseLong(range[1]);

            for (long i = min; i <= max; i++) {
                String num = String.valueOf(i);
                Matcher m = DUPLICATES.matcher(num);
                if (m.find())
                    invalid2 += i;
                int len = num.length();
                if (len % 2 == 0) {
                    String left = num.substring(0, len / 2);
                    String right = num.substring(len / 2);
                    if (left.equals(right))
                        invalid1 += i;
                }
            }
        }
        return Pair.of(invalid1, invalid2);
    }
}

