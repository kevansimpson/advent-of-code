package org.base.advent.code2019;

import org.base.advent.Solution;

import java.util.List;
import java.util.function.ToIntFunction;

/**
 * <a href="https://adventofcode.com/2019/day/01">Day 01</a>
 */
public class Day01 implements Solution<List<Integer>> {
    @Override
    public Object solvePart1(final List<Integer> input) {
        return requiredFuel(input);
    }

    @Override
    public Object solvePart2(final List<Integer> input) {
        return accumulateFuel(input);
    }

    int requiredFuel(final List<Integer> list) {
        return sum(list, this::calculate);
    }

    int accumulateFuel(final List<Integer> list) {
        return sum(list, this::accumulate);
    }

    // to find the fuel required for a module, take its mass, divide by three, round down, and subtract 2.
    int calculate(final int mass) {
        return (int) (double) (mass / 3) - 2;
    }

    int accumulate(final int mass) {
        int fuel = calculate(mass), total = 0;
        while (fuel > 0) {
            total += fuel;
            fuel = calculate(fuel);
        }

        return total;
    }

    private <T> int sum(final List<T> list, final ToIntFunction<T> function) {
        return list.stream().mapToInt(function).sum();
    }
}
