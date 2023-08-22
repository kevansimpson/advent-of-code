package org.base.advent.code2019;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * <a href="https://adventofcode.com/2019/day/04">Day 04</a>
 */
public class Day04 implements Function<IntStream, Day04.PasswordMatches> {
    public record PasswordMatches(long simple, long complex) {}

    @Override
    public PasswordMatches apply(final IntStream intStream) {
        long[] matches = new long[] {0L,0L};
        intStream.forEach(pswd -> {
            if (simple(pswd))
                matches[0] += 1L;
            if (complex(pswd))
                matches[1] += 1L;
        });
        return new PasswordMatches(matches[0], matches[1]);
    }

    boolean simple(final int password) {
        return isOrdered(password) && hasDouble(password);
    }

    boolean complex(final int password) {
        return isOrdered(password) && hasOnlyDouble(password);
    }

    private static final Pattern DOUBLE = Pattern.compile("(\\d)(\\1)");
    boolean hasDouble(final int value) {
        return DOUBLE.matcher(String.valueOf(value)).find();
    }

    boolean hasOnlyDouble(final int value) {
        final String str = String.valueOf(value);
        final Map<String, Integer> map = new HashMap<>();
        for (final String s : str.split(""))
            if (!map.containsKey(s)) map.put(s, StringUtils.countMatches(str, s));

        return map.containsValue(2);
    }

    boolean isOrdered(final int value) {
        final char[] ch = String.valueOf(value).toCharArray();
        Arrays.sort(ch);
        return value == Integer.parseInt(String.valueOf(ch));
    }
}
