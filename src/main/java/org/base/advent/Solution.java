package org.base.advent;

/**
 * Identifies two part solutions from an Advent of Code problem.
 */
public interface Solution<T> extends PuzzleReader {
    Object solvePart1(T input);
    Object solvePart2(T input);
}
