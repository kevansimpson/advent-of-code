package org.base.advent.code2015;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.countMatches;

/**
 * <a href="https://adventofcode.com/2015/day/19">Day 19</a>
 */
public class Day19 implements Function<List<String>, Day19.MedicineMolecule> {
    public record MedicineMolecule(int totalDistinct, int fewestSteps) {}

    @Override
    public MedicineMolecule apply(List<String> replacements) {
        String medicine = replacements.remove(replacements.size() - 1);
        final Set<String> molecules = applyAllReplacements(buildReplacementMap(replacements), medicine);

        return new MedicineMolecule(molecules.size(), shortestPath(medicine));
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
    int shortestPath(final String medicine) {
        int upper = 0;
        for (final char ch : medicine.toCharArray()) {
            if (Character.isUpperCase(ch))
                upper += 1;
        }

        return upper
                - countMatches(medicine, "Rn")
                - countMatches(medicine, "Ar")
                - 2 * countMatches(medicine, "Y") - 1;
    }

    Set<String> applyAllReplacements(final Map<String, List<String>> rmap, final String medicine) {
        final Set<String> molecules = new HashSet<>();
        for (final Entry<String, List<String>> replacement : rmap.entrySet()) {
            final Set<String> uniqueMolecules = applyReplacement(medicine, replacement);
            molecules.addAll(uniqueMolecules);
        }
        return molecules;
    }

    Set<String> applyReplacement(final String chain, final Entry<String, List<String>> replacement) {
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

    Map<String, List<String>> buildReplacementMap(final List<String> replacements) {
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