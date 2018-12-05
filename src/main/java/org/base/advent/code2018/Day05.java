package org.base.advent.code2018;

import org.base.advent.Solution;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import java.util.stream.Collectors;


/**
 * <h2>Part 1</h2>
 * You've managed to sneak in to the prototype suit manufacturing lab. The Elves are making decent progress,
 * but are still struggling with the suit's size reduction capabilities.
 *
 * While the very latest in 1518 alchemical technology might have solved their problem eventually, you can
 * do better. You scan the chemical composition of the suit's material and discover that it is formed by
 * extremely long polymers (one of which is available as your puzzle input).
 *
 * The polymer is formed by smaller units which, when triggered, react with each other such that
 * two adjacent units of the same type and opposite polarity are destroyed.
 * Units' types are represented by letters; units' polarity is represented by capitalization.
 * For instance, r and R are units with the same type but opposite polarity,
 * whereas r and s are entirely different types and do not react.
 *
 * For example:
 * <pre>
 *     - In aA, a and A react, leaving nothing behind.
 *     - In abBA, bB destroys itself, leaving aA. As above, this then destroys itself, leaving nothing.
 *     - In abAB, no two adjacent units are of the same type, and so nothing happens.
 *     - In aabAAB, even though aa and AA are of the same type, their polarities match, and so nothing happens.
 * </pre>
 *
 * Now, consider a larger example, dabAcCaCBAcCcaDA:
 * <pre>
 *     dabAcCaCBAcCcaDA  The first 'cC' is removed.
 *     dabAaCBAcCcaDA    This creates 'Aa', which is removed.
 *     dabCBAcCcaDA      Either 'cC' or 'Cc' are removed (the result is the same).
 *     dabCBAcaDA        No further actions can be taken
 * </pre>
 *
 * After all possible reactions, the resulting polymer contains 10 units.
 * How many units remain after fully reacting the polymer you scanned?
 *
 * <h2>Part 2</h2>
 * Time to improve the polymer.
 *
 * One of the unit types is causing problems; it's preventing the polymer from collapsing as much as it should.
 * Your goal is to figure out which unit type is causing the most problems, remove all instances of it
 * (regardless of polarity), fully react the remaining polymer, and measure its length.
 *
 * For example, again using the polymer dabAcCaCBAcCcaDA from above:
 * <pre>
 *     - Removing all A/a units produces dbcCCBcCcD. Fully reacting this polymer produces dbCBcD, which has length 6.
 *     - Removing all B/b units produces daAcCaCAcCcaDA. Fully reacting this polymer produces daCAcaDA, which has length 8.
 *     - Removing all C/c units produces dabAaBAaDA. Fully reacting this polymer produces daDA, which has length 4.
 *     - Removing all D/d units produces abAcCaCBAcCcaA. Fully reacting this polymer produces abCBAc, which has length 6.
 * </pre>
 *
 * In this example, removing all C/c units was best, producing the answer 4.
 * What is the length of the shortest polymer you can produce by removing all units of exactly one type
 * and fully reacting the result?
 */
public class Day05 implements Solution<String> {

    @Override
    public String getInput() throws IOException {
        return readInput("/2018/input05.txt");
    }

    @Override
    public Object solvePart1() throws Exception {
        return formPolymer(getInput()).length();
    }

    @Override
    public Object solvePart2() throws Exception {
        return improvePolymer(getInput()).length();
    }

    public String improvePolymer(final String input) {
        final Map<Character, Character> rxns = buildReactionMap();
        final Map<Character, String> collapsed = new TreeMap<>();

        for (Character ch : rxns.keySet()) {
            if (collapsed.containsKey(ch)) continue;  // skip lowercase

            final String permutation =
                    input.replaceAll(ch.toString(), "")
                         .replaceAll(rxns.get(ch).toString(), "");
            final String newPolymer = formPolymer(permutation, rxns);
            collapsed.put(ch, newPolymer);
            collapsed.put(rxns.get(ch), newPolymer);
        }

        return collapsed.values().stream()
                .sorted(Comparator.comparingInt(String::length))
                .findFirst().orElseGet(null);
    }

    public String formPolymer(final String input) {
        return formPolymer(input, buildReactionMap());
    }

    private String formPolymer(final String input, final Map<Character, Character> rxns) {
        final char[] polymers = input.toCharArray();
        final Stack<Character> stack = new Stack<>();

        for (final Character ch : polymers)
            if (!stack.isEmpty() && rxns.get(ch) == stack.peek())
                stack.pop();
            else
                stack.push(ch);

        return stack.stream().map(String::valueOf).collect(Collectors.joining());
    }

    Map<Character, Character> buildReactionMap() {
        Map<Character, Character> rxns = new TreeMap<>();

        for (int i = (int) 'A'; i <= (int) 'Z'; i++) {
            final Character ucase = (char) i, lcase = (char) (i + 32);
            rxns.put(ucase, lcase);
            rxns.put(lcase, ucase);
        }

        return rxns;
    }
}
