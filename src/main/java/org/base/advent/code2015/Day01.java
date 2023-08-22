package org.base.advent.code2015;

import org.base.advent.Solution;

/**
 * <a href="https://adventofcode.com/2015/day/01">Day 01</a>
 */
public class Day01 implements Solution<String> {
    @Override
    public Object solvePart1(final String input) {
        return findFloor(input);
    }

    @Override
    public Object solvePart2(final String input) {
        return firstEntersBasement(input);
    }

    int findFloor(String instructions) {
        return instructions.replaceAll("\\)", "").length() -
                instructions.replaceAll("\\(", "").length();
    }

    int firstEntersBasement(final String instructions) {
        int floor = 0;
        int position = 0;
        for (final char ch : instructions.toCharArray()) {
            ++position;
            switch (ch) {
                case ')' -> --floor;
                case '(' -> ++floor;
            }
            if (floor == -1) break;
        }
        
        return position;
    }
}
