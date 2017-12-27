package org.base.advent.code2015;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.base.advent.Solution;

/**
 * <h2>Part 1</h2>
 * Rudolph the Red-Nosed Reindeer is sick! His nose isn't shining very brightly, and he needs medicine.
 *
 * Red-Nosed Reindeer biology isn't similar to regular reindeer biology; Rudolph is going to need custom-made medicine.
 * Unfortunately, Red-Nosed Reindeer chemistry isn't similar to regular reindeer chemistry, either.
 *
 * The North Pole is equipped with a Red-Nosed Reindeer nuclear fusion/fission plant, capable of constructing any
 * Red-Nosed Reindeer molecule you need. It works by starting with some input molecule and then doing a series of
 * replacements, one per step, until it has the right molecule.
 *
 * However, the machine has to be calibrated before it can be used. Calibration involves determining the number of
 * molecules that can be generated in one step from a given starting point.
 *
 * For example, imagine a simpler machine that supports only the following replacements:
 * H => HO
 * H => OH
 * O => HH
 *
 * Given the replacements above and starting with HOH, the following molecules could be generated:
 *  - HOOH (via H => HO on the first H).
 *  - HOHO (via H => HO on the second H).
 *  - OHOH (via H => OH on the first H).
 *  - HOOH (via H => OH on the second H).
 *  - HHHH (via O => HH).
 *
 * So, in the example above, there are 4 distinct molecules (not five, because HOOH appears twice) after one
 * replacement from HOH. Santa's favorite molecule, HOHOHO, can become 7 distinct molecules (over nine replacements:
 * six from H, and three from O).
 *
 * The machine replaces without regard for the surrounding characters. For example, given the string H2O, the
 * transition H => OO would result in OO2O.
 *
 * Your puzzle input describes all of the possible replacements and, at the bottom, the medicine molecule for which
 * you need to calibrate the machine. How many distinct molecules can be created after all the different ways you can
 * do one replacement on the medicine molecule?
 *
 * <h2>Part 2</h2>
 * Now that the machine is calibrated, you're ready to begin molecule fabrication.
 *
 * Molecule fabrication always begins with just a single electron, e, and applying replacements one at a time, just
 * like the ones during calibration.
 *
 * For example, suppose you have the following replacements:
 * e => H
 * e => O
 * H => HO
 * H => OH
 * O => HH
 *
 * If you'd like to make HOH, you start with e, and then make the following replacements:
 *  - e => O to get O
 *  - O => HH to get HH
 *  - H => OH (on the second H) to get HOH
 *
 * So, you could make HOH after 3 steps. Santa's favorite molecule, HOHOHO, can be made in 6 steps.
 *
 * How long will it take to make the medicine? Given the available replacements and the medicine molecule in your
 * puzzle input, what is the fewest number of steps to go from e to the medicine molecule?
 *
 */
public class Day19 implements Solution<List<String>> {
    private Set<String> molecules = new HashSet<>();
    private String medicine;

    @Override
    public List<String> getInput() throws IOException {
        return readLines("/2015/input19.txt");
    }

    @Override
    public Object solvePart1() throws Exception {
        return totalMolecules(getInput());
    }

    @Override
    public Object solvePart2() throws Exception {
        return shortestPath(getInput());
    }
    
    public int totalMolecules(final List<String> replacements) {
        medicine = replacements.remove(replacements.size() - 1);
        applyAllReplacements(buildReplacementMap(replacements));
        return molecules.size();
    }

    /**
     * Adapted from solution found on https://www.reddit.com/r/adventofcode/comments/3xflz8/day_19_solutions/
     * 
     * All of the rules are of one of the following forms:
     * α => βγ
     * α => βRnγAr
     * α => βRnγYδAr
     * α => βRnγYδYεAr
     * 
     * As Rn, Ar, and Y are only on the left side of the equation, one merely only needs to compute
     * 
     *             #NumSymbols - #Rn - #Ar - 2 * #Y - 1
     * 
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
//        int count = 0;
//        for (int index = str.indexOf(x); index >= 0; index = str.indexOf(x, index + 1), ++count) { /* no op */ }
//        return count;
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