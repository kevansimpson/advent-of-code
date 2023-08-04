package org.base.advent.code2019;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.base.advent.Solution;
import org.base.advent.TimeSaver;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <a href="https://adventofcode.com/2019/day/12">Day 12</a>
 */
public class Day12 implements Solution<List<String>>, TimeSaver {
    @Getter
    private final List<String> input =  readLines("/2019/input12.txt");

    @Override
    public Integer solvePart1() {
        return totalEnergy(getInput(), 1000);
    }

    @Override
    public Long solvePart2() {
        return fastOrFull(288684633706728L, () -> completeOrbit(getInput()));
    }

    public int totalEnergy(final List<String> rows, final int steps) {
        final List<Moon> moons = scanMoons(rows);
        for (int s = 0; s < steps; s++) simulate(moons);
        return moons.stream().mapToInt(Moon::energy).sum();
    }

    public long completeOrbit(final List<String> rows) {
        final int[] result = new int[3];
        for (int axis = 0; axis < 3; axis++) result[axis] = completeOrbit(scanMoons(rows), axis);
        return lcm(lcm(result[0], result[1]), result[2]);
    }

    int completeOrbit(final List<Moon> moons, final int axis) {
        final Set<String> moonHashSet = new HashSet<>();
        String hash = hash(moons, axis);

        while (!moonHashSet.contains(hash)) {
            moonHashSet.add(hash);
            simulate(moons);
            hash = hash(moons, axis);
        }

        return moonHashSet.size();
    }

    void simulate(final List<Moon> moons) {
        for (int i = 0, len = moons.size(); i < len; i++) {
            final Moon m1 = moons.get(i);
            for (int j = i; j < len; j++) {
                final Moon m2 = moons.get(j);
                if (StringUtils.equals(m1.name, m2.name)) continue;

                for (int k = 0; k < 3; k++)
                    if (m1.point[k] < m2.point[k]) {
                        m1.velocity[k] += 1;
                        m2.velocity[k] -= 1;
                    } else if (m1.point[k] > m2.point[k]) {
                        m1.velocity[k] -= 1;
                        m2.velocity[k] += 1;
                    }
            }
        }

        moons.forEach(m -> {
            for (int k = 0; k < 3; k++) m.point[k] += m.velocity[k];
        });
    }

    String hash(final List<Moon> moons, final int axis) {
        return moons.stream().map(m -> m.hash(axis)).collect(Collectors.joining("_"));
    }

    private static final List<String> NAMES = Arrays.asList("Io", "Europa", "Ganymede", "Callisto");
    private static final Pattern MOONS = Pattern.compile("-?\\d+");

    List<Moon> scanMoons(final List<String> rows) {
        final Iterator<String> iterator = NAMES.iterator();
        return rows.stream().map(row -> {
            final Matcher m = MOONS.matcher(row);
            return new Moon(iterator.next(), m.results().mapToInt(mr -> Integer.parseInt(mr.group())).toArray());
        }).collect(Collectors.toList());
    }

    @EqualsAndHashCode
    @ToString
    private static class Moon {
        public final int[] point;
        public final int[] velocity = new int[3];
        private final String name;

        public Moon(final String moon, final int... coordinates) {
            point = coordinates;
            name = moon;
        }

        public String hash(final int ix) {
            return String.format("%s(%d,%d)", name, point[ix], velocity[ix]);
        }

        public int energy() {
            return sum(point) * sum(velocity);
        }

        private int sum(final int... vals) {
            int sum = 0;
            for (int val : vals) sum += Math.abs(val);
            return sum;
        }
    }

    /**
     * Lowest common multiple.
     * <a href="https://www.baeldung.com/java-least-common-multiple">Baeldung to the rescue</a>.
     */
    public static long lcm(long number1, long number2) {
        if (number1 == 0 || number2 == 0) {
            return 0;
        }
        long absNumber1 = Math.abs(number1);
        long absNumber2 = Math.abs(number2);
        long absHigherNumber = Math.max(absNumber1, absNumber2);
        long absLowerNumber = Math.min(absNumber1, absNumber2);
        long lcm = absHigherNumber;
        while (lcm % absLowerNumber != 0)  lcm += absHigherNumber;
        return lcm;
    }
}
