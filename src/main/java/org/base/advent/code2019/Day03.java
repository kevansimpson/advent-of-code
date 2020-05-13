package org.base.advent.code2019;

import lombok.Getter;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.Solution;
import org.base.advent.util.Point;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * <h2>Part 1</h2>
 * The gravity assist was successful, and you're well on your way to the Venus refuelling station. During the rush back
 * on Earth, the fuel management system wasn't completely installed, so that's next on the priority list.
 *
 * Opening the front panel reveals a jumble of wires. Specifically, two wires are connected to a central port and extend
 * outward on a grid. You trace the path each wire takes as it leaves the central port, one wire per line of text (your
 * puzzle input).
 *
 * The wires twist and turn, but the two wires occasionally cross paths. To fix the circuit, you need to find the
 * intersection point closest to the central port. Because the wires are on a grid, use the Manhattan distance for this
 * measurement. While the wires do technically cross right at the central port where they both start, this point does
 * not count, nor does a wire count as crossing with itself.
 *
 * For example, if the first wire's path is R8,U5,L5,D3, then starting from the central port (o), it goes right 8, up 5,
 * left 5, and finally down 3:
 * <pre>
 * ...........
 * ...........
 * ...........
 * ....+----+.
 * ....|....|.
 * ....|....|.
 * ....|....|.
 * .........|.
 * .o-------+.
 * ...........
 * </pre>
 *
 *Then, if the second wire's path is U7,R6,D4,L4, it goes up 7, right 6, down 4, and left 4:
 * <pre>
 * ...........
 * .+-----+...
 * .|.....|...
 * .|..+--X-+.
 * .|..|..|.|.
 * .|.-X--+.|.
 * .|..|....|.
 * .|.......|.
 * .o-------+.
 * ...........
 * </pre>
 *
 * These wires cross at two locations (marked X), but the lower-left one is closer to the central port: its distance is 3 + 3 = 6.
 *
 * Here are a few more examples:
 * <pre>
 *   - R75,D30,R83,U83,L12,D49,R71,U7,L72
 *     U62,R66,U55,R34,D71,R55,D58,R83 = distance 159
 *   - R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51
 *     U98,R91,D20,R16,D67,R40,U7,R15,U6,R7 = distance 135
 * </pre>
 * What is the Manhattan distance from the central port to the closest intersection?
 *
 * <h2>Part 2</h2>
 */
public class Day03 implements Solution<List<String[]>> {

    @Override
    public List<String[]> getInput() throws IOException {
        return readCSVLines("/2019/input03.txt");
    }

    @Override
    public Object solvePart1() throws Exception {
        return closestIntersection(getInput());
    }

    @Override
    public Object solvePart2() throws Exception {
        return fewestSteps(getInput());
    }

    // expects and uses only 2 wires
    public int fewestSteps(final List<String[]> jumbledWires) {
        final Pair<WirePath, WirePath> untangled = untangle(jumbledWires);
        return analyzePath(untangled, pt -> countStepsAt(pt, untangled));
    }

    // expects and uses only 2 wires
    public int closestIntersection(final List<String[]> jumbledWires) {
        return analyzePath(untangle(jumbledWires), Point::getManhattanDistance);
    }

    protected int analyzePath(Pair<WirePath, WirePath> untangled, final Function<Point, Integer> function) {
        return SetUtils.intersection(
                new HashSet<>(untangled.getLeft().getPath()), new HashSet<>(untangled.getRight().getPath()))
                .stream()
                .map(function)
                .min(Comparator.naturalOrder()).orElse(-1);
    }

    protected int countStepsAt(final Point point, final Pair<WirePath, WirePath> untangled) {
        return untangled.getLeft().getSteps().getOrDefault(point, 0) +
                untangled.getRight().getSteps().getOrDefault(point, 0);
    }

    protected Pair<WirePath, WirePath> untangle(final List<String[]> jumbles) {
        final List<WirePath> untangled = jumbles.stream().map(this::runWires).collect(Collectors.toList());
        return Pair.of(untangled.get(0), untangled.get(1));
    }

    protected WirePath runWires(final String... wire) {
        final List<Point> path = new LinkedList<>();
        final Map<Point, Integer> steps = new HashMap<>();
        Point pt = Point.ORIGIN;
        int count = 0;
        for (final String w : wire) {
            final String dir = w.substring(0, 1);
            final int dist = Integer.parseInt(w.substring(1));
            for (int x = 1; x <= dist; x++) {
                pt = Point.MOVE_MAP.get(dir).apply(pt);
                path.add(pt);
                ++count;
                if (!steps.containsKey(pt)) steps.put(pt, count);
            }
        }

        return new WirePath(path, steps);
    }

    @Getter
    private static class WirePath {
        private final List<Point> path;
        private final Map<Point, Integer> steps;

        public WirePath(final List<Point> p, final Map<Point, Integer> s) {
            path = p;
            steps = s;
        }
    }
}
