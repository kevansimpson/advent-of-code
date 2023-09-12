package org.base.advent.code2022;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.TimeSaver;
import org.base.advent.util.Point;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.Math.abs;
import static org.base.advent.util.Text.extractInt;
import static org.base.advent.util.Util.merge;

/**
 * <a href="https://adventofcode.com/2022/day/15">Day 15</a>
 */
public class Day15 implements Function<List<String>, Day15.HandheldDevice>, TimeSaver {
    public record HandheldDevice(long noBeacons, long tuningFrequency) {}

    @Override
    public HandheldDevice apply(final List<String> input) {
        final List<Sensor> sensors = input.stream().map(line -> new Sensor(extractInt(line))).toList();
        final Set<Point> beacons = sensors.stream().map(Sensor::getBeacon).collect(Collectors.toSet());
        final long ptsWithoutBeacons = overlap(sensors, 2000000L, beacons);
        try {
            for (long row = fullOrFast(0L, 3349000L); row <= 4000000L; row += 1L)
                overlap(sensors, row, beacons);
        }
        catch (Answer answer) {
            return new HandheldDevice(ptsWithoutBeacons, answer.result);
        }
        throw new IllegalStateException("Day15,2022");
    }

    long overlap(final List<Sensor> sensors, final long row, final Set<Point> beacons) {
        List<Range<Long>> ranges = sensors.stream()
                .map(it -> it.inRange(row))
                .filter(Objects::nonNull)
                .sorted((a, b) -> Math.toIntExact(a.getMinimum() - b.getMinimum()))
                .toList();
        final AtomicReference<Range<Long>> overlap = new AtomicReference<>(Range.of(0L, 0L));
        for (Range<Long> range : ranges) {
            Pair<Range<Long>, Range<Long>> pair = merge(overlap.get(), range);
            if (pair.getRight() != null) {
                if (pair.getRight().getMinimum() - 2L == pair.getLeft().getMaximum()) // GAP!
                    throw new Answer((pair.getRight().getMinimum() - 1L) * 4000000L + row);
            }
            overlap.set(pair.getLeft());
        }

        long remove = beacons.stream().filter(b -> (b.y == row) && overlap.get().contains(b.x)).count();
        return (overlap.get().getMaximum() - overlap.get().getMinimum() + 1L) - remove;
    }

    private static class Sensor {
        @Getter
        private final Point position;
        @Getter
        private final Point beacon;
        private final long dist;

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
