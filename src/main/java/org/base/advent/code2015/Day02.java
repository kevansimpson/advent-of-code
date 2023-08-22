package org.base.advent.code2015;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * <a href="https://adventofcode.com/2015/day/02">Day 02</a>
 */
public class Day02 implements Function<List<String>, Day02.WrappedGift> {
    public record WrappedGift(int paper, int ribbon) {}

    @Override
    public WrappedGift apply(final List<String> dimensions) {
        int total = 0;
        int ribbon = 0;

        for (final String dimStr : dimensions) {
            final List<Integer> p = Arrays.asList(
                    Integer.parseInt(StringUtils.substringBefore(dimStr, "x")),
                    Integer.parseInt(StringUtils.substringBetween(dimStr, "x")),
                    Integer.parseInt(StringUtils.substringAfterLast(dimStr, "x")));
            Collections.sort(p);
            total += needsWrappingPaper(p);
            ribbon += needsRibbon(p);
        }

        return new WrappedGift(total, ribbon);
    }

    int needsWrappingPaper(final List<Integer> p) {
        // adding an extra of first side for slack
        return (3 * p.get(0) * p.get(1)) + (2 * p.get(1) * p.get(2)) + (2 * p.get(2) * p.get(0));
    }

    int needsRibbon(final List<Integer> p) {
        return (2 * p.get(0)) + (2 * p.get(1)) + /*bow*/ (p.get(0) * p.get(1) * p.get(2));
    }
}
