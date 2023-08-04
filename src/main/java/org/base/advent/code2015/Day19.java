package org.base.advent.code2015;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.base.advent.Solution;

import java.util.*;
import java.util.Map.Entry;

/**
 * <a href="https://adventofcode.com/2015/day/19">Day 19</a>
 */
public class Day19 implements Solution<List<String>> {
    private final Set<String> molecules = new HashSet<>();
    private String medicine;

    @Getter
    private final List<String> input = readLines("/2015/input19.txt");
    @Getter
    private final List<String> input2 = readLines("/2015/input19.txt");

    @Override
    public Object solvePart1() {
        return totalMolecules(getInput());
    }

    @Override
    public Object solvePart2() {
        return shortestPath(input2);
    }
    
    public int totalMolecules(final List<String> replacements) {
        medicine = replacements.remove(replacements.size() - 1);
        applyAllReplacements(buildReplacementMap(replacements));
        return molecules.size();
    }

    /**
     * Adapted from solution found on
     * <a href="https://www.reddit.com/r/adventofcode/comments/3xflz8/day_19_solutions/">reddit</a>.
     * <pre>
     * All the rules are of one of the following forms:
     * α => βγ
     * α => βRnγAr
     * α => βRnγYδAr
     * α => βRnγYδYεAr
     * </pre>
     * As Rn, Ar, and Y are only on the left side of the equation, one merely only needs to compute
     * <pre>
     *             #NumSymbols - #Rn - #Ar - 2 * #Y - 1
     * </pre>
     * Subtract of #Rn and #Ar because those are just extras. 
     * Subtract two times #Y because we get rid of the Ys and the extra elements following them. 
     * Subtract one because we start with "e".
     *
     */
    public int shortestPath(final List<String> replacements) {
        medicine = replacements.remove(replacements.size() - 1);

        int upper = 0;
        for (final char ch : medicine.toCharArray()) {
            if (Character.isUpperCase(ch))
                upper += 1;
        }

        return upper
                - countOccurrences(medicine, "Rn")
                - countOccurrences(medicine, "Ar")
                - 2 * countOccurrences(medicine, "Y") - 1;
    }

    protected int countOccurrences(final String str, final String x) {
        return StringUtils.countMatches(str, x);
    }

    protected void applyAllReplacements(final Map<String, List<String>> rmap) {
        for (final Entry<String, List<String>> replacement : rmap.entrySet()) {
            final Set<String> uniqueMolecules = applyReplacement(medicine, replacement);
            molecules.addAll(uniqueMolecules);
        }
    }

    protected Set<String> applyReplacement(final String chain, final Entry<String, List<String>> replacement) {
        final Set<String> uniqueMolecules = new HashSet<>();
        for (final String repl : replacement.getValue()) {
            int start = 0, index;
            while ((index = chain.indexOf(replacement.getKey(), start)) >= 0) {
                final String variation = chain.substring(0, index) + repl +
                        chain.substring(index + replacement.getKey().length());
                uniqueMolecules.add(variation);
                start = index + replacement.getKey().length();
            }
        }

        return uniqueMolecules;
    }

    protected Map<String, List<String>> buildReplacementMap(final List<String> replacements) {
        final Map<String, List<String>> rmap = new HashMap<>();
        for (final String replacement : replacements) {
            if (StringUtils.isBlank(replacement)) continue;
            final String[] tokens = replacement.split("\\s");
            if (!rmap.containsKey(tokens[0])) {
                rmap.put(tokens[0], new ArrayList<>());
            }
            rmap.get(tokens[0]).add(tokens[2]);
        }
        return rmap;
    }
}