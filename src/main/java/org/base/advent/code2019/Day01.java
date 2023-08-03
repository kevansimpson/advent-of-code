package org.base.advent.code2019;

import org.base.advent.Solution;
import org.base.advent.util.Util;

import java.io.IOException;
import java.util.List;


/**
 * <a href="https://adventofcode.com/2019/day/01">Day 01</a>
 */
public class Day01 implements Solution<List<Integer>> {

    @Override
    public List<Integer> getInput(){
        return readNumbers("/2019/input01.txt");
    }

    @Override
    public Object solvePart1() {
        return requiredFuel(getInput());
    }

    @Override
    public Object solvePart2() {
        return accumulateFuel(getInput());
    }

    public int requiredFuel(final List<Integer> list) {
        return Util.sum(list, this::calculate);
    }

    public int accumulateFuel(final List<Integer> list) {
        return Util.sum(list, this::accumulate);
    }

    // to find the fuel required for a module, take its mass, divide by three, round down, and subtract 2.
    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    protected int calculate(final int mass) {
        return (int) Math.floor(mass / 3) - 2;
    }

    protected int accumulate(final int mass) {
        int fuel = calculate(mass), total = 0;
        while (fuel > 0) {
            total += fuel;
            fuel = calculate(fuel);
        }

        return total;
    }
}
