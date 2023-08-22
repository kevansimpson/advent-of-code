package org.base.advent.code2015;

import lombok.Getter;
import org.base.advent.Solution;

import java.util.List;
import java.util.function.Function;

/**
 * <a href="https://adventofcode.com/2015/day/17">Day 17</a>
 */
public class Day17 implements Function<List<String>, Day17.CanPermutations> {
    private int totalPermutations;
    private int fewestCans = Integer.MAX_VALUE;
    private int totalPermutationsWithFewest;

    public record CanPermutations(int total, int fewest) {}

    @Override
    public CanPermutations apply(List<String> input) {
        int[] cans = input.stream().map(Integer::parseInt).mapToInt(i->i).toArray();
        final boolean[] permutation = new boolean[cans.length];
        
        sumCans(permutation, 0, cans);
        return new CanPermutations(totalPermutations, totalPermutationsWithFewest);
    }

    void sumCans(final boolean[] permutation, final int index, int[] cans) {
        int numberOfCans = cans.length;
        if (index >= numberOfCans) {
            if (sum(permutation, cans) == 150) {
                totalPermutations += 1;

                final int used = used(permutation, numberOfCans);
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

        sumCans(off, 1 + index, cans);
        on[index] = true;
        sumCans(on, 1 + index, cans);
    }

    int sum(final boolean[] permutation, int[] cans) {
        int sum = 0;
        for (int i = 0; i < cans.length; i++)
            sum += (permutation[i]) ? cans[i] : 0;

        return sum;
    }

    int used(final boolean[] permutation, int numberOfCans) {
        int used = 0;
        for (int i = 0; i < numberOfCans; i++)
            used += (permutation[i]) ? 1 : 0;

        return used;
    }
}
