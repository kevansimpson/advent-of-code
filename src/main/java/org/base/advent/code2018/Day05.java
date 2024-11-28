package org.base.advent.code2018;

import org.base.advent.Solution;

import java.util.*;

/**
 * <a href="https://adventofcode.com/2018/day/05">Day 05</a>
 */
public class Day05 implements Solution<String> {
    @Override
    public Object solvePart1(final String input) {
        return formPolymer(input).length();
    }

    @Override
    public Object solvePart2(final String input) {
        return improvePolymer(input).length();
    }

    String improvePolymer(final String input) {
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

        //noinspection DataFlowIssue
        return collapsed.values().stream().min(Comparator.comparingInt(String::length)).orElseGet(null);
    }

    String formPolymer(final String input) {
        return formPolymer(input, buildReactionMap());
    }

    private String formPolymer(final String input, final Map<Character, Character> rxns) {
        final char[] polymers = input.toCharArray();
        final Deque<Character> stack = new ArrayDeque<>();

        for (final Character ch : polymers) {
            if (!stack.isEmpty() && rxns.get(ch) == stack.peek())
                stack.pop();
            else
                stack.push(ch);
        }

        StringBuilder bldr = new StringBuilder(stack.size());
        while (!stack.isEmpty())
            bldr.append(stack.removeLast());
        return bldr.toString();
    }

    Map<Character, Character> buildReactionMap() {
        Map<Character, Character> rxns = new TreeMap<>();

        for (int i = 'A'; i <= (int) 'Z'; i++) {
            final Character ucase = (char) i, lcase = (char) (i + 32);
            rxns.put(ucase, lcase);
            rxns.put(lcase, ucase);
        }

        return rxns;
    }
}
