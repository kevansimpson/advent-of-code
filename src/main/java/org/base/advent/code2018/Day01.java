package org.base.advent.code2018;

import org.base.advent.Solution;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * <a href="https://adventofcode.com/2018/day/01">Day 01</a>
 */
public class Day01 implements Solution<List<Integer>> {

    @Override
    public List<Integer> getInput(){
        return readNumbers("/2018/input01.txt");
    }

    @Override
    public Object solvePart1() {
        return sum(getInput());
    }

    @Override
    public Object solvePart2() {
        return findDuplicateFrequency(getInput());
    }

    public int sum(final List<Integer> list) {
        return list.stream().mapToInt(Integer::intValue).sum();
    }

    public int findDuplicateFrequency(final List<Integer> list) {
        final Set<Integer> frequencies = new TreeSet<>();
        int sum = 0;
        frequencies.add(sum);

        while (true) {
            for (int value : list) {
                sum += value;
                if (frequencies.contains(sum))
                    return sum;
                else
                    frequencies.add(sum);
            }
        }
    }
}
