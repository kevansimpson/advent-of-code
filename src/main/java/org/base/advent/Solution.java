package org.base.advent;

/**
 * Identifies two part solutions from an Advent of Code problem.
 */
public interface Solution<T> extends PuzzleReader {

    T getInput();

    Object solvePart1();
    Object solvePart2();
}
