package org.base.advent.code2015;

import org.apache.commons.lang3.StringUtils;
import org.base.advent.Solution;
import org.base.advent.TimeSaver;

import java.util.*;

/**
 * <a href="https://adventofcode.com/2015/day/24">Day 24</a>
 */
@SuppressWarnings("OptionalGetWithoutIsPresent")
public class Day24 implements Solution<List<Integer>>, TimeSaver {

    private int smallestGroup = Integer.MAX_VALUE;
    private long lowestQE = Long.MAX_VALUE;
    private int expectedSum = -1;

    @Override
    public Object solvePart1(final List<Integer> input) {
        return fastOrFull(11846773891L, () -> solveFor(new ArrayList<>(input), 3));
    }

    @Override
    public Object solvePart2(final List<Integer> input) {
        return solveFor(new ArrayList<>(input), 4);
    }

    long solveFor(final List<Integer> containers, final int numCompartments) {
        smallestGroup = Integer.MAX_VALUE;
        lowestQE = Long.MAX_VALUE;
        findSmallest(containers, numCompartments);
        solve(containers, 0, new ArrayList<>(), 0);
        return lowestQE;
    }
    
    protected void findSmallest(final List<Integer> containers, final int numCompartments) {
        Collections.reverse(containers);
        final int max = 2 << containers.size() - 1;
        expectedSum = containers.stream().mapToInt(Integer::intValue).sum() / numCompartments;

        for (int i = 0; i < max; i++) {
            final int[] ia = toArray(i, containers);
            final int is = Arrays.stream(ia).sum();
            if (ia.length == 0 || is != expectedSum) continue;

            if (ia.length < smallestGroup) {
                smallestGroup = ia.length;
                lowestQE = calcQE(ia);
                return;
            }
        }
    }


    protected void solve(final List<Integer> containers, final int index, final List<Integer> permutation, final int currentSum) {
        if (currentSum == expectedSum) {
            if (permutation.size() == smallestGroup) {
                final long qe = permutation.stream().mapToLong(Long::valueOf).reduce((a,b) -> a * b).getAsLong();
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
            final int value = containers.get(i);
            if (permutation.contains(value)) continue;
            final List<Integer> next = new ArrayList<>(permutation);
            next.add(value);
            solve(containers, 1 + index, next, value + currentSum);
        }
    }

    protected long calcQE(final int[] ints) {
        return Arrays.stream(ints).mapToLong(l -> l).reduce((l1, l2) -> l1 * l2).getAsLong();
    }

    protected int[] toArray(final int flag, final List<Integer> cs) {
        final Set<Integer> set = new TreeSet<>();
        final String reverse = StringUtils.reverse(Integer.toBinaryString(flag));
        for (int i = 0; i < reverse.length(); i += 1)
            if ('1' == reverse.charAt(i))
                set.add(cs.get(i));

        return set.stream().mapToInt(Integer::intValue).toArray();
    }

}