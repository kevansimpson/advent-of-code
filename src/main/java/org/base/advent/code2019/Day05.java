package org.base.advent.code2019;

import lombok.Getter;
import org.base.advent.Solution;
import org.base.advent.code2019.intCode.Program;

/**
 * <a href="https://adventofcode.com/2019/day/05">Day 05</a>
 */
public class Day05 implements Solution<int[]> {
    @Getter
    private final int[] input =  readNumbersCSV("/2019/input05.txt");

    @Override
    public Object solvePart1() {
        return Program.runProgram(() -> 1, getInput()).getOutput();
    }

    @Override
    public Object solvePart2() {
        return Program.runProgram(() -> 5, getInput()).getOutput();
    }
}
