package org.base.advent.code2017;

import org.base.advent.util.HexPoint;
import org.base.advent.util.Util;

import java.util.function.Function;

/**
 * <a href="https://adventofcode.com/2017/day/11">Day 11</a>
 */
public class Day11 implements Function<String, Util.MinMaxLong> {
    @Override
    public Util.MinMaxLong apply(final String directions) {
        HexPoint point = HexPoint.CENTER;
        final String[] steps = directions.split(",");
        long max = 0;

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

        return new Util.MinMaxLong(HexPoint.hexDistance(point), max);
    }
}
