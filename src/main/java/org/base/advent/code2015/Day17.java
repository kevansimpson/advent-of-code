package org.base.advent.code2015;

import lombok.Getter;
import org.base.advent.Solution;

import java.util.List;

/**
 * <a href="https://adventofcode.com/2015/day/17">Day 17</a>
 */
public class Day17 implements Solution<List<String>> {
    private int[] cans;
    private int numberOfCans;
    private int totalPermutations;
    private int fewestCans = Integer.MAX_VALUE;
    private int totalPermutationsWithFewest;

    @Getter
    private final List<String> input = readLines("/2015/input17.txt");

    @Override
    public Object solvePart1() {
        return totalCanPermutations(getInput());
    }

    @Override
    public Object solvePart2() {
        return fewestCanPermutations(getInput());
    }
    
    public int totalCanPermutations(final List<String> input) {
        cans = input.stream().map(Integer::parseInt).mapToInt(i->i).toArray();
        numberOfCans = cans.length;
        final boolean[] permutation = new boolean[numberOfCans];
        
        sumCans(permutation, 0);
        return totalPermutations;
    }

    public int fewestCanPermutations(final List<String> input) {
        if (cans == null)
            totalCanPermutations(input);
        return totalPermutationsWithFewest;
    }

    protected void sumCans(final boolean[] permutation, final int index) {
        if (index >= numberOfCans) {
            if (sum(permutation) == 150) {
                totalPermutations += 1;

                final int used = used(permutation);
                if (used < fewestCans) {
                    fewestCans = used;
                    totalPermutationsWithFewest = 1;
                }
                else if (used == fewestCans) {
                    totalPermutationsWithFewest += 1;
                }
            }
            return;
        }

        final boolean[] off = new boolean[numberOfCans];
        final boolean[] on = new boolean[numberOfCans];
        
        System.arraycopy(permutation, 0, off, 0, numberOfCans);
        System.arraycopy(permutation, 0, on, 0, numberOfCans);

        sumCans(off, 1 + index);
        on[index] = true;
        sumCans(on, 1 + index);
    }

    protected int sum(final boolean[] permutation) {
        int sum = 0;
        for (int i = 0; i < numberOfCans; i++)
            sum += (permutation[i]) ? cans[i] : 0;

        return sum;
    }

    protected int used(final boolean[] permutation) {
        int used = 0;
        for (int i = 0; i < numberOfCans; i++)
            used += (permutation[i]) ? 1 : 0;

        return used;
    }
}
