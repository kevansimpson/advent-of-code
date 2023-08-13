package org.base.advent.code2022;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.Solution;
import org.base.advent.TimeSaver;
import org.base.advent.util.Point;
import org.base.advent.util.Util;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Math.abs;
import static org.base.advent.util.Util.extractInt;

/**
 * <a href="https://adventofcode.com/2022/day/15">Day 15</a>
 */
public class Day15 implements Solution<List<String>>, TimeSaver {
    @Getter
    private final List<String> input = readLines("/2022/input15.txt");
    private final List<Sensor> sensors = input.stream().map(line -> new Sensor(extractInt(line))).toList();
    private final Set<Point> beacons = sensors.stream().map(Sensor::getBeacon).collect(Collectors.toSet());

    @Override
    public Object solvePart1() {
        return overlap(sensors, 2000000L);
    }

    @Override
    public Object solvePart2() {
        try {
            for (long row = fullOrFast(0L, 3349000L); row <= 4000000L; row += 1L)
                overlap(sensors, row);
        }
        catch (Answer answer) {
            return answer.result;
        }
        return 1138;
    }

    public long overlap(List<Sensor> sensors, long row) {
        List<Range<Long>> ranges = sensors.stream()
                .map(it -> it.inRange(row))
                .filter(Objects::nonNull)
                .sorted((a, b) -> Math.toIntExact(a.getMinimum() - b.getMinimum()))
                .toList();
        final Range<Long>[] overlap = new Range[] { Range.of(0L, 0L) };
        for (Range<Long> range : ranges) {
            Pair<Range<Long>, Range<Long>> pair = merge(overlap[0], range);
            if (pair.getRight() != null) {
                if (pair.getRight().getMinimum() - 2L == pair.getLeft().getMaximum()) // GAP!
                    throw new Answer((pair.getRight().getMinimum() - 1L) * 4000000L + row);
            }
            overlap[0] = pair.getLeft();
        }

        long remove = beacons.stream().filter(b -> (b.y == row) && overlap[0].contains((long) b.x)).count();
        return (overlap[0].getMaximum() - overlap[0].getMinimum() + 1L) - remove;
    }

    /**
     * Merges two {@link Range} into a single one, unless there's no overlap.
     * The merged <code>Range</code> will usually be the {@link Pair#getLeft()} of returned <code>Pair</code>.
     * If there's no overlap, the <code>Range</code> parameters are returned in sorted order in a {@link Pair}.
     *
     * @param one the first <code>Range</code> parameter.
     * @param two the second <code>Range</code> parameter.
     * @return a pair of <code>Range</code>, one of which will be <code>null</code> unless there's overlap.
     */
    static Pair<Range<Long>, Range<Long>> merge(Range<Long> one, Range<Long> two) {
        if (one.getMinimum() <= two.getMinimum()) {
            if (one.getMaximum() >= two.getMaximum())       // one contains two
                return Pair.of(one, null);
            else if (one.getMaximum() < two.getMinimum())   // no overlap
                return Pair.of(one, two);
            else                                            // one + two
                return Pair.of(Range.of(one.getMinimum(), two.getMaximum()), null);
        }
        else {
            if (one.getMaximum() <= two.getMaximum())       // two contains one
                return Pair.of(two, null);
            else if (one.getMinimum() > two.getMaximum())   // no overlap
                return Pair.of(two, one);
            else                                            // two + one
                return Pair.of(Range.of(two.getMinimum(), one.getMaximum()), null);
        }
    }

    private static class Sensor {
        @Getter
        private final Point position;
        @Getter
        private final Point beacon;
        private final int dist;

        public Sensor(int[] ints) {
            this(Point.of(ints[0], ints[1]), Point.of(ints[2], ints[3]));
        }

        public Sensor(Point pt, Point bcn) {
            position = pt;
            beacon = bcn;
            dist = position.getManhattanDistance(beacon);
        }

        public Range<Long> inRange(long row) {
            long y = abs(row - position.y);
            if (y > dist)
                return null;
            else {
                long diff = abs(y - dist);
                return Range.of((position.x - diff), (position.x + diff));
            }
        }
    }

    @AllArgsConstructor
    private static class Answer extends RuntimeException {
        private final long result;
    }
}
