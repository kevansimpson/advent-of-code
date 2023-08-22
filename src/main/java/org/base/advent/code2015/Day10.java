package org.base.advent.code2015;


import java.util.function.Function;

/**
 * <a href="https://adventofcode.com/2015/day/10">Day 10</a>
 */
public class Day10 implements Function<String, Day10.LookAndSay> {
    public record LookAndSay(int firstLength, int secondLength) {}

    @Override
    public LookAndSay apply(final String input) {
        String first = lookAndSay(input, 0, 40);
        return new LookAndSay(first.length(), lookAndSay(first, 40, 50).length());
    }

    String lookAndSay(String input, final int start, final int count) {
        for (int i = start; i < count; i++) {
            input = lookAndSay(input);
        }
        return input;
    }
    
    String lookAndSay(final String input) {
        int count = 0;
        char digit = input.charAt(0);
        final StringBuilder bldr = new StringBuilder();

        for (final char ch : input.toCharArray()) {
            if (digit == ch) {
                count +=1;
            }
            else {
                bldr.append(count).append(digit);
                digit = ch;
                count = 1;
            }
        }

        bldr.append(count).append(digit);
        return bldr.toString();
    }
}
