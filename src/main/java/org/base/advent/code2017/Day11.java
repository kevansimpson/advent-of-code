package org.base.advent.code2017;

import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.Solution;
import org.base.advent.util.HexPoint;

/**
 * <a href="https://adventofcode.com/2017/day/11">Day 11</a>
 */
public class Day11 implements Solution<String> {
    @Getter
    private final String input =  readInput("/2017/input11.txt");

    @Override
    public Object solvePart1() {
        return countSteps(getInput()).getLeft();
    }

    @Override
    public Object solvePart2() {
        return countSteps(getInput()).getRight();
    }

    public Pair<Integer, Integer> countSteps(final String directions) {
        HexPoint point = HexPoint.CENTER;
        final String[] steps = directions.split(",");
        int max = 0;

        for (final String step : steps) {
            switch (step) {
                case "n" -> point = point.north();
                case "s" -> point = point.south();
                case "nw" -> point = point.northwest();
                case "sw" -> point = point.southwest();
                case "ne" -> point = point.northeast();
                case "se" -> point = point.southeast();
            }

            max = Math.max(max, HexPoint.hexDistance(point));
        }

        return Pair.of(HexPoint.hexDistance(point), max);
    }
}
