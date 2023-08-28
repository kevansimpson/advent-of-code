package org.base.advent.code2015;

import org.apache.commons.lang3.ObjectUtils;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <a href="https://adventofcode.com/2015/day/16">Day 16</a>
 */
public class Day16 implements Function<List<String>, Day16.AuntSue> {
    private static final String INDEX = "INDEX";

    private static final Map<String, Integer> tickerTape = Map.of("children", 3,
        "cats", 7, "samoyeds", 2, "pomeranians", 3,
        "akitas", 0, "vizslas", 0, "goldfish", 5,
        "trees", 3, "cars", 2, "perfumes", 1);

    public record AuntSue(int giftingSue, int realSue) {}

    @Override
    public AuntSue apply(final List<String> input) {
        final List<Map<String, Integer>> gift = new ArrayList<>();
        final List<Map<String, Integer>> real = new ArrayList<>();
        for (final String aSueList : input) {
            final Map<String, Integer> attr = parseSue(aSueList);
            if (hasSameAttr(attr)) {
                gift.add(attr);
            }
            if (hasSameRanges(attr)) {
                real.add(attr);
            }
        }

        return new AuntSue(gift.get(0).get(INDEX), real.get(0).get(INDEX));
    }

    boolean hasSameRanges(final Map<String, Integer> attr) {
        return satisfiesTicker(attr) && reverseTicker(attr);
    }

    boolean satisfiesTicker(final Map<String, Integer> attr) {
        for (final String key : tickerTape.keySet()) {
            switch (key) {
                case INDEX -> {
                }
                case "trees", "cats" -> {
                    if (attr.containsKey(key) && ObjectUtils.compare(tickerTape.get(key), attr.get(key)) >= 0) {
                        return false;
                    }
                }
                case "pomeranians", "goldfish" -> {
                    if (attr.containsKey(key) && ObjectUtils.compare(tickerTape.get(key), attr.get(key)) <= 0) {
                        return false;
                    }
                }
                default -> {
                    if (attr.containsKey(key) && !Objects.equals(tickerTape.get(key), attr.get(key))) {
                        return false;
                    }
                }
            }
        }
        
        return true;
    }

    boolean reverseTicker(final Map<String, Integer> attr) {
        for (final String key : attr.keySet()) {
            switch (key) {
                case INDEX, "trees", "cats", "pomeranians", "goldfish" -> {
                }
                default -> {
                    if (tickerTape.containsKey(key) && !Objects.equals(attr.get(key), tickerTape.get(key))) {
                        return false;
                    }
                }
            }
        }
        
        return true;
    }

    boolean hasSameAttr(final Map<String, Integer> attr) {
        return hasSameValues(tickerTape, attr) && hasSameValues(attr, tickerTape);
    }

    boolean hasSameValues(final Map<String, Integer> attr1, final Map<String, Integer> attr2) {
        for (final String key : attr1.keySet()) {
            if (INDEX.equals(key)) continue;

            if (attr2.containsKey(key) && !Objects.equals(attr1.get(key), attr2.get(key)))
                return false;
        }

        return true;
    }

    protected Map<String, Integer> parseSue(final String sue) {
        final Pattern parser = Pattern.compile(
                "\\w+ (\\d+): (\\w+): (\\d+), (\\w+): (\\d+), (\\w+): (\\d+)", Pattern.DOTALL);
        final Map<String, Integer> attr = new HashMap<>();
        final Matcher m = parser.matcher(sue);
        if (m.matches()) {
            attr.put(INDEX, Integer.parseInt(m.group(1)));
            attr.put(m.group(2), Integer.parseInt(m.group(3)));
            attr.put(m.group(4), Integer.parseInt(m.group(5)));
            attr.put(m.group(6), Integer.parseInt(m.group(7)));
        }
        else {
            throw new RuntimeException("No match: "+ sue);
        }
        return attr;
    }
}
