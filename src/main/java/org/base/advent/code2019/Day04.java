package org.base.advent.code2019;

import org.apache.commons.lang3.StringUtils;
import org.base.advent.Solution;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.IntStream;


/**
 * <h2>Part 1</h2>
 * You arrive at the Venus fuel depot only to discover it's protected by a password. The Elves had written the password
 * on a sticky note, but someone threw it out.
 *
 * However, they do remember a few key facts about the password:
 * <ul>
 *      <li>It is a six-digit number.</li>
 *      <li>The value is within the range given in your puzzle input.</li>
 *      <li>Two adjacent digits are the same (like 22 in 122345).</li>
 *      <li>Going from left to right, the digits never decrease;
 *          they only ever increase or stay the same (like 111123 or 135679).</li>
 * </ul>
 * Other than the range rule, the following are true:
 * <ul>
 *      <li>111111 meets these criteria (double 11, never decreases).</li>
 *      <li>223450 does not meet these criteria (decreasing pair of digits 50).</li>
 *      <li>123789 does not meet these criteria (no double).</li>
 * </ul>
 *
 * How many different passwords within the range given in your puzzle input meet these criteria?
 *
 * <h2>Part 2</h2>
 * An Elf just remembered one more important detail: the two adjacent matching digits are not part of a larger group of
 * matching digits.
 *
 * Given this additional criterion, but still ignoring the range rule, the following are now true:
 * <ul>
 *      <li>112233 meets these criteria because the digits never decrease and all repeated digits are exactly two digits long.</li>
 *      <li>123444 no longer meets the criteria (the repeated 44 is part of a larger group of 444).</li>
 *      <li>111122 meets the criteria (even though 1 is repeated more than twice, it still contains a double 22).</li>
 * </ul>
 *
 * How many different passwords within the range given in your puzzle input meet all of the criteria?
 */
public class Day04 implements Solution<IntStream> {

    @Override
    public IntStream getInput() throws IOException {
        return IntStream.rangeClosed(235741, 706948);
    }

    @Override
    public Object solvePart1() throws Exception {
        return simpleMatch(getInput());
    }

    @Override
    public Object solvePart2() throws Exception {
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
