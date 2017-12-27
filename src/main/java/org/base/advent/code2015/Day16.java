package org.base.advent.code2015;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.ObjectUtils;
import org.base.advent.Solution;

/**
 * <h2>Part 1</h2>
 * Your Aunt Sue has given you a wonderful gift, and you'd like to send her a thank you card. However, there's a
 * small problem: she signed it "From, Aunt Sue".
 *
 * You have 500 Aunts named "Sue".
 *
 * So, to avoid sending the card to the wrong person, you need to figure out which Aunt Sue (which you conveniently
 * number 1 to 500, for sanity) gave you the gift. You open the present and, as luck would have it, good ol' Aunt Sue
 * got you a My First Crime Scene Analysis Machine! Just what you wanted. Or needed, as the case may be.
 *
 * The My First Crime Scene Analysis Machine (MFCSAM for short) can detect a few specific compounds in a given sample,
 * as well as how many distinct kinds of those compounds there are. According to the instructions, these are what
 * the MFCSAM can detect:
 *
 *  - children, by human DNA age analysis.
 *  - cats. It doesn't differentiate individual breeds.
 *  - Several seemingly random breeds of dog: samoyeds, pomeranians, akitas, and vizslas.
 *  - goldfish. No other kinds of fish.
 *  - trees, all in one group.
 *  - cars, presumably by exhaust or gasoline or something.
 *  - perfumes, which is handy, since many of your Aunts Sue wear a few kinds.
 *
 * In fact, many of your Aunts Sue have many of these. You put the wrapping from the gift into the MFCSAM. It beeps
 * inquisitively at you a few times and then prints out a message on ticker tape:
 *
 * children: 3
 * cats: 7
 * samoyeds: 2
 * pomeranians: 3
 * akitas: 0
 * vizslas: 0
 * goldfish: 5
 * trees: 3
 * cars: 2
 * perfumes: 1
 *
 * You make a list of the things you can remember about each Aunt Sue. Things missing from your list aren't zero - you
 * simply don't remember the value.
 *
 * What is the number of the Sue that got you the gift?
 *
 * <h2>Part 2</h2>
 * As you're about to send the thank you note, something in the MFCSAM's instructions catches your eye. Apparently, it
 * has an outdated retroencabulator, and so the output from the machine isn't exact values - some of them indicate ranges.
 *
 * In particular, the cats and trees readings indicates that there are greater than that many (due to the unpredictable
 * nuclear decay of cat dander and tree pollen), while the pomeranians and goldfish readings indicate that there are
 * fewer than that many (due to the modial interaction of magnetoreluctance).
 *
 * What is the number of the real Aunt Sue?
 */
public class Day16 implements Solution<List<String>> {

    private static final String INDEX = "INDEX";

    @Override
    public List<String> getInput() throws IOException {
        return readLines("/2015/input16.txt");
    }

    @Override
    public Object solvePart1() throws Exception {
        return auntSueIndex(getInput());
    }

    @Override
    public Object solvePart2() throws Exception {
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

    private static Pattern parser = Pattern.compile(
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
