package org.base.advent.code2022;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Delegate;
import org.apache.commons.collections4.SetUtils;
import org.base.advent.Solution;
import org.base.advent.util.Point;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.stream.Collectors.groupingBy;

/**
 * <a href="https://adventofcode.com/2022/day/17">Day 17</a>
 */
public class Day17 implements Solution<String> {
    @Getter @Setter
    private String input = readInput("/2022/input17.txt");

    @Override
    public Object solvePart1() {
        return dropRocks(newCavern(), 2022L);
    }

    @Override
    public Object solvePart2() {
        return dropRocks(newCavern(), 1000000000000L);
    }

    public long dropRocks(Cavern cavern, long rockCount) {
        RockHistory history = new RockHistory(cavern);
        while (cavern.rockIndex < rockCount) {
            CavernState state = cavern.dropRock();
            if (history.containsKey(state)) {
                return history.expectedHeight(rockCount, cavern.rockIndex - 1L, history.get(state));
            }
            else {
                history.put(state, new RockPattern(cavern.rockIndex - 1L, cavern.height));
            }
        }
        return cavern.height;
    }

    Cavern newCavern() {
        return new Cavern(getInput().toCharArray());
    }

    record RockHistory(@Delegate
                       Map<CavernState, RockPattern> cache,
                       Cavern cavern) implements Map<CavernState, RockPattern> {
        public RockHistory(Cavern c) {
            this(new HashMap<>(), c);
        }

        public long expectedHeight(long rockCount, long r, RockPattern pattern) {
            long perLoop = r - pattern.rocksFallen;
            long loopHeight = cavern.height - pattern.cavernHeight;
            long loopsLeft = (rockCount - r) / perLoop;
            long rocksLeft = (rockCount - r) % perLoop;
            long heightLeft = cache.values().stream()
                    .filter(it -> it.rocksFallen == (pattern.rocksFallen + rocksLeft - 1))
                    .findFirst().orElseThrow().cavernHeight - pattern.cavernHeight;
            return cavern.height + loopsLeft * loopHeight + heightLeft;
        }
    }

    @RequiredArgsConstructor
    @ToString(exclude = {"winds", "rocks"})
    static class Cavern {
        private final char[] winds;
        private final Set<Point> rocks = new HashSet<>();
        private long windex = 0;
        private long height = 0;
        private long numRocks = -1L;
        private long rockIndex = 0L;

        public CavernState dropRock() {
            int rix = (int) (rockIndex++ % 5L);
            RockBuilder bldr = RockBuilder.values()[rix];
            Point highest = Point.of(2, 3 + height);
            Rock rock = bldr.apply(highest);
            drop(rock);
            numRocks = rockIndex;

            return getStateAt(rix);
        }

        public void drop(Rock rock) {
            Rock falling = rock;
            while (true) {
                char wind = nextWind();
                Rock blown = falling.blown(wind);
                if (blown.leftEdge >= 0 && blown.rightEdge < 7 && SetUtils.intersection(rocks, blown.points).isEmpty())
                    falling = blown;

                Rock drop = falling.drop();
                if (drop.bottom >= 0 && SetUtils.intersection(rocks, drop.points).isEmpty())
                    falling = drop;
                else {
                    rocks.addAll(falling.points);
                    height = max(height, falling.top + 1); // zero-index
                    return;
                }
            }
        }

        public CavernState getStateAt(long rockIndex) {
            Map<Long, List<Point>> map = rocks.stream().collect(groupingBy(it -> it.x));
            if (map.size() < 7) // fill empty columns, if necessary
                LongStream.range(0L, 7L).forEach(x ->
                    map.computeIfAbsent(x, key -> Collections.singletonList(Point.of(x, -1L))));
            // grab the highest rock in each column as a footprint of fallen rocks
            List<Long> list = map.values().stream()
                    .map(points -> points.stream().mapToInt(Point::iy).max().orElse(0))
                    .map(it -> it - height)
                    .toList();
            return new CavernState(list, windex % winds.length, rockIndex + 1);
        }

        char nextWind() {
            return winds[(int) (windex++ % winds.length)];
        }

        public void display() {
            System.out.println();
            for (long y = height + 3; y >= 0; y--) {
                System.out.print("|");
                for (long x = 0; x < 7; x ++) {
                    if (rocks.contains(Point.of(x, y)))
                        System.out.print("#");
                    else
                        System.out.print(".");
                }
                System.out.println("|");
            }
            // floor
            System.out.println("+-------+");
        }
    }

    record CavernState(List<Long> top, long windex, long rockShape) {}
    record RockPattern(long rocksFallen, long cavernHeight) {}

    @Getter
    static class Rock {
        private final Set<Point> points;
        private int leftEdge = 8;
        private int rightEdge = -1;
        private long top = -1;
        private int bottom = Integer.MAX_VALUE;

        public Rock(Set<Point> pts) {
            points = pts;
            for (Point pt : points) {
                leftEdge = min(leftEdge, pt.ix());
                rightEdge = max(rightEdge, pt.ix());
                top = max(top, pt.y);
                bottom = min(bottom, pt.iy());
            }
        }

        public Rock blown(char dir) {
            return new Rock(points.stream().map(pt -> pt.move(String.valueOf(dir))).collect(Collectors.toSet()));
        }

        public Rock drop() {
            return new Rock(points.stream().map(pt -> pt.down(1)).collect(Collectors.toSet()));
        }
    }

    enum RockBuilder implements Function<Point, Rock> {
        Horizontal() {
            @Override
            public Rock apply(Point start) {
                return new Rock(Set.of(start, start.right(1), start.right(2), start.right(3)));
            }
        },
        Cross() {
            @Override
            public Rock apply(Point start) {
                return new Rock(Set.of(start.right(1) /* bottom */, start.move(1, 2) /* top */,
                        start.up(1), start.move(1, 1), start.move(2, 1)));
            }
        },
        Reversal() {    // "reverse-L"
            @Override
            public Rock apply(org.base.advent.util.Point start) {
                return new Rock(Set.of(start, start.right(1), start.right(2),
                        start.move(2, 1), start.move(2, 2)));
            }
        },
        Vertical() {
            @Override
            public Rock apply(Point start) {
                return new Rock(Set.of(start, start.up(1), start.up(2), start.up(3)));
            }
        },
        Square() {
            @Override
            public Rock apply(Point start) {
                return new Rock(Set.of(start, start.right(1), start.up(1), start.move(1, 1)));
            }
        }
    }
}
