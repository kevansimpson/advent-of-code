package org.base.advent.code2019;

import org.apache.commons.lang3.StringUtils;
import org.base.advent.Solution;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * <a href="https://adventofcode.com/2019/day/04">Day 04</a>
 */
public class Day04 implements Solution<IntStream> {

    @Override
    public IntStream getInput(){
        return IntStream.rangeClosed(235741, 706948);
    }

    @Override
    public Object solvePart1() {
        return simpleMatch(getInput());
    }

    @Override
    public Object solvePart2() {
        return complexMatch(getInput());
    }

    public long simpleMatch(final IntStream passwords) {
        return passwords.filter(this::simple).count();
    }

    public long complexMatch(final IntStream passwords) {
        return passwords.filter(this::complex).count();
    }

    protected boolean simple(int password) {
        return isOrdered(password) && hasDouble(password);
    }

    protected boolean complex(int password) {
        return isOrdered(password) && hasOnlyDouble(password);
    }

    private static final Pattern DOUBLE = Pattern.compile("(\\d)(\\1)");
    protected boolean hasDouble(final int value) {
        return DOUBLE.matcher(String.valueOf(value)).find();
    }

    protected boolean hasOnlyDouble(final int value) {
        final String str = String.valueOf(value);
        final Map<String, Integer> map = new HashMap<>();
        for (final String s : str.split(""))
            if (!map.containsKey(s)) map.put(s, StringUtils.countMatches(str, s));

        return map.containsValue(2);
    }

    protected boolean isOrdered(final int value) {
        final char[] ch = String.valueOf(value).toCharArray();
        Arrays.sort(ch);
        return value == Integer.parseInt(String.valueOf(ch));
    }
}
