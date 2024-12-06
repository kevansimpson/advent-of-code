package org.base.advent.code2024;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static org.base.advent.util.Util.splitByBlankLine;

/**
 * <a href="https://adventofcode.com/2024/day/5">Day 5</a>
 */
public class Day05 implements Function<List<String>, Day05.SafetyManual> {
    public record SafetyManual(int middleSum, int corrected) {}

    @Override
    public SafetyManual apply(List<String> input) {
        List<List<String>> rulesUpdates = splitByBlankLine(input);
        RulesMap rules = new RulesMap().initialize(rulesUpdates.get(0));
        int middleSum = 0, corrected = 0;
        for (String line : rulesUpdates.get(1)) {
            int[] update = Stream.of(line.split(",")).mapToInt(Integer::parseInt).toArray();
            if (inRightOrder(update, rules))
                middleSum += update[update.length / 2];
            else
               corrected += reorderUpdate(update, rules);
        }

        return new SafetyManual(middleSum, corrected);
    }

    private boolean inRightOrder(int[] update, RulesMap rules) {
        for (int i = 0; i < update.length - 1; i++)
            for (int j = i + 1; j < update.length; j++)
                if (rules.isBefore(update, i, j))
                    return false;

        return true;
    }

    private int reorderUpdate(int[] update, RulesMap rules) {
        return IntStream.of(update).boxed().sorted(rules).toList().get(update.length / 2);
    }

    private static class RulesMap implements Comparator<Integer> {
        private final Map<Integer, List<Integer>> before = new HashMap<>();

        @Override
        public int compare(Integer a, Integer b) {
            if (isBefore(b, a))
                return -1;
            else if (a.equals(b))
                return 0;
            else
                return 1;
        }

        public boolean isBefore(int i, int j) {
            return  (!before.containsKey(i) || !before.getOrDefault(i, emptyList()).contains(j));
        }

        public boolean isBefore(int[] update, int i, int j) {
            return  (!before.containsKey(update[i]) || !before.get(update[i]).contains(update[j]));
        }

        public RulesMap initialize(List<String> input) {
            for (String line : input) {
                int[] rule = Stream.of(line.split("\\|")).mapToInt(Integer::parseInt).toArray();
                if (!before.containsKey(rule[0]))
                    before.put(rule[0], new ArrayList<>());
                before.get(rule[0]).add(rule[1]);
            }

            return this;
        }
    }
}

