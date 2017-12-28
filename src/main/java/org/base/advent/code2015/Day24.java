package org.base.advent.code2015;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.base.advent.Solution;

/**
 * <h2>Part 1</h2>
 * It's Christmas Eve, and Santa is loading up the sleigh for this year's deliveries. However, there's one small
 * problem: he can't get the sleigh to balance. If it isn't balanced, he can't defy physics, and nobody gets
 * presents this year.
 *
 * No pressure.
 *
 * Santa has provided you a list of the weights of every package he needs to fit on the sleigh. The packages need
 * to be split into three groups of exactly the same weight, and every package has to fit. The first group goes in
 * the passenger compartment of the sleigh, and the second and third go in containers on either side. Only when all
 * three groups weigh exactly the same amount will the sleigh be able to fly. Defying physics has rules, you know!
 *
 * Of course, that's not the only problem. The first group - the one going in the passenger compartment - needs as
 * few packages as possible so that Santa has some legroom left over. It doesn't matter how many packages are in
 * either of the other two groups, so long as all of the groups weigh the same.
 *
 * Furthermore, Santa tells you, if there are multiple ways to arrange the packages such that the fewest possible
 * are in the first group, you need to choose the way where the first group has the smallest quantum entanglement to
 * reduce the chance of any "complications". The quantum entanglement of a group of packages is the product of their
 * weights, that is, the value you get when you multiply their weights together. Only consider quantum entanglement
 * if the first group has the fewest possible number of packages in it and all groups weigh the same amount.
 *
 * For example, suppose you have ten packages with weights 1 through 5 and 7 through 11. For this situation, some of
 * the unique first groups, their quantum entanglements, and a way to divide the remaining packages are as follows:
 * 
 * Group 1;             Group 2;  Group 3
 * 11 9       (QE= 99); 10 8 2;    7 5 4 3 1
 * 10 9 1     (QE= 90); 11 7 2;    8 5 4 3
 * 10 8 2     (QE=160); 11 9;      7 5 4 3 1
 * 10 7 3     (QE=210); 11 9;      8 5 4 2 1
 * 10 5 4 1   (QE=200); 11 9;      8 7 3 2
 * 10 5 3 2   (QE=300); 11 9;      8 7 4 1
 * 10 4 3 2 1 (QE=240); 11 9;      8 7 5
 * 9 8 3      (QE=216); 11 7 2;   10 5 4 1
 * 9 7 4      (QE=252); 11 8 1;   10 5 3 2
 * 9 5 4 2    (QE=360); 11 8 1;   10 7 3
 * 8 7 5      (QE=280); 11 9;     10 4 3 2 1
 * 8 5 4 3    (QE=480); 11 9;     10 7 2 1
 * 7 5 4 3 1  (QE=420); 11 9;     10 8 2
 * 
 * Of these, although 10 9 1 has the smallest quantum entanglement (90), the configuration with only two packages,
 * 11 9, in the passenger compartment gives Santa the most legroom and wins. In this situation, the quantum
 * entanglement for the ideal configuration is therefore 99. Had there been two configurations with only two
 * packages in the first group, the one with the smaller quantum entanglement would be chosen.
 * 
 * What is the quantum entanglement of the first group of packages in the ideal configuration?
 * 
 * <h2>Part 2</h2>
 * That's weird... the sleigh still isn't balancing.
 *
 * "Ho ho ho", Santa muses to himself. "I forgot the trunk".
 *
 * Balance the sleigh again, but this time, separate the packages into four groups instead of three.
 * The other constraints still apply.
 *
 * Given the example packages above, this would be some of the new unique first groups, their quantum entanglements,
 * and one way to divide the remaining packages:
 *
 * 11 4    (QE=44); 10 5;   9 3 2 1; 8 7
 * 10 5    (QE=50); 11 4;   9 3 2 1; 8 7
 * 9 5 1   (QE=45); 11 4;   10 3 2;  8 7
 * 9 4 2   (QE=72); 11 3 1; 10 5;    8 7
 * 9 3 2 1 (QE=54); 11 4;   10 5;    8 7
 * 8 7     (QE=56); 11 4;   10 5;    9 3 2 1
 *
 * Of these, there are three arrangements that put the minimum (two) number of packages in the first
 * group: 11 4, 10 5, and 8 7. Of these, 11 4 has the lowest quantum entanglement, and so it is selected.
 *
 * Now, what is the quantum entanglement of the first group of packages in the ideal configuration?
 *
 */
public class Day24 implements Solution<List<String>> {

    private int smallestGroup = Integer.MAX_VALUE;
    private long lowestQE = Long.MAX_VALUE;
    private int expectedSum = -1;

    @Override
    public List<String> getInput() throws IOException {
        return readLines("/2015/input24.txt");
    }

    @Override
    public Object solvePart1() throws Exception {
        return solveFor(getInput(), 3);
    }

    @Override
    public Object solvePart2() throws Exception {
        return solveFor(getInput(), 4);
    }


    private static Function<List<String>, int[]> toIntArray =
            ls -> ls.stream().mapToInt(Integer::parseInt).toArray();

    protected long solveFor(final List<String> containers, final int numCompartments) {
        smallestGroup = Integer.MAX_VALUE;
        lowestQE = Long.MAX_VALUE;

        findSmallest(toIntArray.apply(containers), numCompartments);
        solve(containers, 0, new ArrayList<>(), 0);
        
//        debug(answer, " in ", (System.currentTimeMillis() - ts), "ms");
//        info("Lowest QE for ", numCompartments, " compartments is ", lowestQE);
        return lowestQE;
    }
    
    void findSmallest(int[] containers, int numCompartments) {
        ArrayUtils.reverse(containers);
        int max = 2 << containers.length - 1;
        expectedSum = Arrays.stream(containers).sum() / numCompartments;

        for (int i = 0; i < max; i++) {
            int[] ia = toArray(i, containers);
            int is = Arrays.stream(ia).sum();
            if (ia.length <= 0 || is != expectedSum) continue;

            if (ia.length < smallestGroup) {
                smallestGroup = ia.length;
                lowestQE = calcQE(ia);;
                return;
            }
        }
    }
    void solve(List<String> containers, int index, List<Integer> permutation, int currentSum) {
        if (currentSum == expectedSum) {
            if (permutation.size() < smallestGroup) {
                lowestQE = permutation.stream().mapToLong(Long::valueOf).reduce((a,b) -> a * b).getAsLong();
                smallestGroup = permutation.size();
                debug("answer = %s", permutation);
            }
            else if (permutation.size() == smallestGroup) {
                long qe = permutation.stream().mapToLong(Long::valueOf).reduce((a,b) -> a * b).getAsLong();
                if (qe < lowestQE) {
                    lowestQE = qe;
                    debug("answer = %s", permutation);
                }
            }
            return;
        }

        if (index >= containers.size() || permutation.size() >= smallestGroup)
            return;

        for (int i = 0; i < containers.size(); i++) {
            int value = Integer.parseInt(containers.get(i));
            if (permutation.contains(value)) continue;
            List<Integer> next = new ArrayList<>(permutation);
            next.add(value);
            solve(containers, 1 + index, next, value + currentSum);
        }
    }

    long calcQE(int[] ints) {
        return Arrays.stream(ints).mapToLong(l -> l).reduce((l1, l2) -> l1 * l2).getAsLong();
    }

    int[] toArray(int flag, int... cs) {
        Set<Integer> set = new TreeSet<>();
        String rbin = StringUtils.reverse(Integer.toBinaryString(flag));
        for (int i = 0; i < rbin.length(); i += 1)
            if ('1' == rbin.charAt(i))
                set.add(cs[i]);

        return set.stream().mapToInt(e -> e.intValue()).toArray();
    }

}