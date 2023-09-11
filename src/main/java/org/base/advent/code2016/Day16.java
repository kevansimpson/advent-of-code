package org.base.advent.code2016;

import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.reverse;

/**
 * <a href="https://adventofcode.com/2016/day/16">Day 16</a>
 */
public class Day16 implements Function<String, Day16.DragonCurve> {
    public record DragonCurve(String checksum1, String checksum2) {}

    @Override
    public DragonCurve apply(String input) {
        return new DragonCurve(curve(input, 272), curve(input, 35651584));
    }

    String curve(String data, int targetLength) {
        String input = data;
        while (input.length() < targetLength)
            input = String.format("%s0%s", input, flip(reverse(input)));

        return checksum(input.substring(0, targetLength));
    }

    String checksum(String data) {
        StringBuilder ckSum = new StringBuilder(data.length());
        for (int i = 0; i < data.length(); i += 2) {
            ckSum.append((data.charAt(i) == data.charAt(i + 1)) ? '1' : '0');
        }

        return (ckSum.length() % 2) == 0 ? checksum(ckSum.toString()) : ckSum.toString();
    }

    String flip(String data) {
        StringBuilder builder = new StringBuilder(data.length());
        for (char bit : data.toCharArray())
            builder.append((bit == '0') ? '1' : '0');

        return builder.toString();
    }
}