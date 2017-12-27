package org.base.advent.code2015;

import org.base.advent.Solution;

import java.io.IOException;
import java.util.List;

/**
 * <h2>Part 1</h2>
 * The elves bought too much eggnog again - 150 liters this time. To fit it all into your refrigerator, you'll need
 * to move it into smaller containers. You take an inventory of the capacities of the available containers.
 *
 * For example, suppose you have containers of size 20, 15, 10, 5, and 5 liters. If you need to store 25 liters,
 * there are four ways to do it:
 *
 *  - 15 and 10
 *  - 20 and 5 (the first 5)
 *  - 20 and 5 (the second 5)
 *  - 15, 5, and 5
 *
 * Filling all containers entirely, how many different combinations of containers can exactly fit all 150 liters of eggnog?
 *
 * <h2>Part 2</h2>
 * While playing with all the containers in the kitchen, another load of eggnog arrives! The shipping and receiving
 * department is requesting as many containers as you can spare.
 *
 * Find the minimum number of containers that can exactly fit all 150 liters of eggnog. How many different ways can
 * you fill that number of containers and still hold exactly 150 litres?
 *
 * In the example above, the minimum number of containers was two. There were three ways to use that many containers,
 * and so the answer there would be 3.w
 */
public class Day17 implements Solution<List<String>> {
    private int[] cans;
    private int numberOfCans;
    private int totalPermutations;
    private int fewestCans = Integer.MAX_VALUE;
    private int totalPermutationsWithFewest;

    @Override
    public List<String> getInput() throws IOException {
        return readLines("/2015/input17.txt");
    }

    @Override
    public Object solvePart1() throws Exception {
        return totalCanPermutations(getInput());
    }

    @Override
    public Object solvePart2() throws Exception {
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
