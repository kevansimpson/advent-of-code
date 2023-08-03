package org.base.advent.code2015;

import org.apache.commons.lang3.ObjectUtils;
import org.base.advent.Solution;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <a href="https://adventofcode.com/2015/day/16">Day 16</a>
 */
public class Day16 implements Solution<List<String>> {

    private static final String INDEX = "INDEX";

    @Override
    public List<String> getInput(){
        return readLines("/2015/input16.txt");
    }

    @Override
    public Object solvePart1() {
        return auntSueIndex(getInput());
    }

    @Override
    public Object solvePart2() {
        return outdatedRetroencabulator(getInput());
    }


    public int auntSueIndex(final List<String> sueList) {
        final Map<String, Integer> tickerTape = buildTickerTape();

        final List<Map<String, Integer>> potentials = new ArrayList<>();
        for (final String aSueList : sueList) {
            final Map<String, Integer> attr = parseSue(aSueList);
            if (hasSameAttr(tickerTape, attr)) {
                potentials.add(attr);
            }
        }

        return potentials.get(0).get(INDEX);
    }

    public int outdatedRetroencabulator(final List<String> sueList) {
        final Map<String, Integer> tickerTape = buildTickerTape();

        final List<Map<String, Integer>> potentials = new ArrayList<>();
        for (final String aSueList : sueList) {
            final Map<String, Integer> attr = parseSue(aSueList);
            if (hasSameRanges(tickerTape, attr)) {
                potentials.add(attr);
            }
        }

        return potentials.get(0).get(INDEX);
    }

    protected boolean hasSameRanges(final Map<String, Integer> attr1, final Map<String, Integer> attr2) {
        return satisfiesTicker(attr1, attr2) && reverseTicker(attr2, attr1);
    }

    protected boolean satisfiesTicker(final Map<String, Integer> tickerTape, final Map<String, Integer> attr) {
        for (final String key : tickerTape.keySet()) {
            switch (key) {
                case INDEX:
                    continue;
                case "trees":
                case "cats":
                    if (attr.containsKey(key) && ObjectUtils.compare(tickerTape.get(key), attr.get(key)) >= 0) {
                        return false;
                    }
                    break;
                case "pomeranians":
                case "goldfish":
                    if (attr.containsKey(key) && ObjectUtils.compare(tickerTape.get(key), attr.get(key)) <= 0) {
                        return false;
                    }
                    break;
                default:
                    if (attr.containsKey(key) && !Objects.equals(tickerTape.get(key), attr.get(key))) {
                        return false;
                    }
            }
        }
        
        return true;
    }

    protected boolean reverseTicker(final Map<String, Integer> attr, final Map<String, Integer> tickerTape) {
        for (final String key : attr.keySet()) {
            switch (key) {
                case INDEX:
                case "trees":
                case "cats":
                case "pomeranians":
                case "goldfish":
                    continue;
                default:
                    if (tickerTape.containsKey(key) && !Objects.equals(attr.get(key), tickerTape.get(key))) {
                        return false;
                    }
            }
        }
        
        return true;
    }

    protected boolean hasSameAttr(final Map<String, Integer> attr1, final Map<String, Integer> attr2) {
        return hasSameValues(attr1, attr2) && hasSameValues(attr2, attr1);
    }

    protected boolean hasSameValues(final Map<String, Integer> attr1, final Map<String, Integer> attr2) {
        for (final String key : attr1.keySet()) {
            if (INDEX.equals(key)) continue;

            if (attr2.containsKey(key) && !Objects.equals(attr1.get(key), attr2.get(key)))
                return false;
        }

        return true;
    }

    protected Map<String, Integer> buildTickerTape() {
        final Map<String, Integer> tickerTape = new HashMap<>();
        tickerTape.put("children", 3);
        tickerTape.put("cats", 7);
        tickerTape.put("samoyeds", 2);
        tickerTape.put("pomeranians", 3);
        tickerTape.put("akitas", 0);
        tickerTape.put("vizslas", 0);
        tickerTape.put("goldfish", 5);
        tickerTape.put("trees", 3);
        tickerTape.put("cars", 2);
        tickerTape.put("perfumes", 1);

        return tickerTape;
    }

    private static final Pattern parser = Pattern.compile(
            "\\w+ (\\d+): (\\w+): (\\d+), (\\w+): (\\d+), (\\w+): (\\d+)", Pattern.DOTALL);

    protected Map<String, Integer> parseSue(final String sue) {
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
