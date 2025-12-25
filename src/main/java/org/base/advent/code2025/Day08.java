package org.base.advent.code2025;

import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.util.Point3D;

import java.util.*;
import java.util.function.Function;

/**
 * <a href="https://adventofcode.com/2025/day/8">Day 8</a>
 */
public class Day08 implements Function<List<String>, Pair<Integer, Long>> {
    record Circuit(Point3D a, Point3D b, double distance) {}
    record CircuitGroup(Set<Point3D> points, Set<Circuit> circuits) {
        CircuitGroup() {
            this(new HashSet<>(), new HashSet<>());
        }

        CircuitGroup add(Circuit c) {
            points.add(c.a);
            points.add(c.b);
            circuits.add(c);
            return this;
        }
    }
    record Playground(Map<Point3D, CircuitGroup> groups, List<Circuit> list, Set<Point3D> points) {
        long loop(int start, int end) {
            for (int i = start; i < end; i++) {
                Circuit c = list.get(i);
                CircuitGroup groupA = groups.get(c.a);
                CircuitGroup groupB = groups.get(c.b);
                if (groupA == null) {
                    if (groupB == null) {
                        CircuitGroup newGroup = new CircuitGroup().add(c);
                        groups.put(c.a, newGroup);
                        groups.put(c.b, newGroup);
                    }
                    else {
                        groupB.add(c);
                        groups.put(c.a, groupB);
                        groups.put(c.b, groupB);
                    }
                }
                else {
                    groupA.add(c);
                    if (groupB != null) { // merge
                        groupB.points.forEach(pt -> groups.put(pt, groupA));
                        groupA.points.addAll(groupB.points);
                        groupA.circuits.addAll(groupB.circuits);
                    }
                    groups.put(c.a, groupA);
                    groups.put(c.b, groupA);
                }

                points.removeAll(groups.keySet());
                if (points.isEmpty()) {
                    return c.a.x * c.b.x;
                }
            }
            return -1;
        }

        int part1() {
            Set<CircuitGroup> circuitGroups =
                    new TreeSet<>(Comparator.comparingInt(g -> -g.points.size()));
            circuitGroups.addAll(groups.values());
            int product = 1;
            Iterator<CircuitGroup> iter = circuitGroups.iterator();
            for (int i = 0; i < 3; i++) {
                product *= iter.next().points.size();
            }
            return product;
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
        Set<Circuit> circuits = new TreeSet<>(Comparator.comparingDouble(Circuit::distance));
        for (Point3D a : points) {
            for (Point3D b : points) {
                if (a != b) {
                    circuits.add(new Circuit(a, b, a.euclideanDistance(b)));
                }
            }
        }

        Map<Point3D, CircuitGroup> groups = new HashMap<>();
        List<Circuit> list = new ArrayList<>(circuits);
        Set<Point3D> pointSet = new HashSet<>(points);
        Playground playground = new Playground(groups, list, pointSet);
        CircuitGroup first = new CircuitGroup().add(list.get(0));
        first.points.forEach(pt -> {
            groups.put(pt, first);
            pointSet.remove(pt);
        });
        playground.loop(1, count);
        int part1 = playground.part1();
        long part2 = playground.part2(count);

        return Pair.of(part1, part2);
    }

}

