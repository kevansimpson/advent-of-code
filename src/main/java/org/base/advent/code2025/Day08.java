package org.base.advent.code2025;

import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.util.Point3D;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * <a href="https://adventofcode.com/2025/day/8">Day 8</a>
 */
public class Day08 implements Function<List<String>, Pair<Integer, Long>> {
    record Circuit(Point3D a, Point3D b, double distance) {}
    record CircuitGroup(Set<Point3D> points) {
        CircuitGroup() {
            this(new HashSet<>());
        }

        CircuitGroup add(Circuit c) {
            points.add(c.a);
            points.add(c.b);
            return this;
        }
    }
    record Playground(Map<Point3D, CircuitGroup> groups, List<Circuit> list, AtomicInteger points) {
        long loop(int start, int end) {
            int remaining = points.get();
            for (int i = start; i < end; i++) {
                Circuit c = list.get(i);
                CircuitGroup groupA = groups.get(c.a);
                CircuitGroup groupB = groups.get(c.b);
                if (groupA == null) {
                    if (groupB == null) {
                        CircuitGroup newGroup = new CircuitGroup().add(c);
                        groups.put(c.a, newGroup);
                        groups.put(c.b, newGroup);
                        remaining -= 2;
                    }
                    else {
                        groupB.add(c);
                        groups.put(c.a, groupB);
                        remaining -= 1;
                    }
                }
                else {
                    groupA.add(c);
                    groups.put(c.b, groupA);
                    if (groupB != null) { // merge
                        groupB.points.forEach(pt -> groups.put(pt, groupA));
                        groupA.points.addAll(groupB.points);
                    }
                    else
                        remaining -= 1;
                }

                if (remaining == 0)
                    return c.a.x * c.b.x;
                else
                    points.set(remaining);
            }
            return -1;
        }

        int part1() {
            return groups.values().stream()
                    .distinct()
                    .mapToInt(g -> g.points.size())
                    .boxed()
                    .sorted(Comparator.reverseOrder())
                    .limit(3)
                    .reduce(1, (a, b) -> a * b);
        }

        long part2(int start) {
            return loop(start, list.size());
        }
    }

    private final int count;

    public Day08(int count) {
        this.count = count;
    }

    @Override
    public Pair<Integer, Long> apply(List<String> input) {
        List<Point3D> points = input.stream().map(Point3D::point3D).toList();
        List<Circuit> circuits = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                Point3D a = points.get(i);
                Point3D b = points.get(j);
                circuits.add(new Circuit(a, b, a.euclideanDistance(b)));
            }
        }
        circuits.sort(Comparator.comparingDouble(Circuit::distance));

        Map<Point3D, CircuitGroup> groups = new HashMap<>(points.size() * 2);
        Playground playground = new Playground(groups, circuits, new AtomicInteger(points.size() - 2));
        CircuitGroup first = new CircuitGroup().add(circuits.get(0));
        first.points.forEach(pt -> groups.put(pt, first));
        playground.loop(1, count);
        int part1 = playground.part1();
        long part2 = playground.part2(count);

        return Pair.of(part1, part2);
    }

}

