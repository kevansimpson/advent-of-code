package org.base.advent.code2018;

import org.base.advent.Solution;
import org.base.advent.util.Point;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * <h2>Part 1</h2>
 * The device on your wrist beeps several times, and once again you feel like you're falling.
 *
 * "Situation critical," the device announces. "Destination indeterminate. Chronal interference detected.
 * Please specify new target coordinates."
 *
 * The device then produces a list of coordinates (your puzzle input). Are they places it thinks are safe
 * or dangerous? It recommends you check manual page 729. The Elves did not give you a manual.
 *
 * If they're dangerous, maybe you can minimize the danger by finding the coordinate that gives the largest
 * distance from the other points.
 *
 * Using only the Manhattan distance, determine the area around each coordinate by counting the number of
 * integer X,Y locations that are closest to that coordinate (and aren't tied in distance to any other coordinate).
 *
 * Your goal is to find the size of the largest area that isn't infinite. For example, consider the following
 * list of coordinates:
 * <pre>
 *     1, 1
 *     1, 6
 *     8, 3
 *     3, 4
 *     5, 5
 *     8, 9
 * </pre>
 *
 * If we name these coordinates A through F, we can draw them on a grid, putting 0,0 at the top left:
 * <pre>
 *     ..........
 *     .A........
 *     ..........
 *     ........C.
 *     ...D......
 *     .....E....
 *     .B........
 *     ..........
 *     ..........
 *     ........F.
 * </pre>
 *
 * This view is partial - the actual grid extends infinitely in all directions. Using the
 * Manhattan distance, each location's closest coordinate can be determined, shown here in lowercase:
 * <pre>
 *     aaaaa.cccc
 *     aAaaa.cccc
 *     aaaddecccc
 *     aadddeccCc
 *     ..dDdeeccc
 *     bb.deEeecc
 *     bBb.eeee..
 *     bbb.eeefff
 *     bbb.eeffff
 *     bbb.ffffFf
 * </pre>
 *
 * Locations shown as . are equally far from two or more coordinates, and so they don't count as being closest to any.
 *
 * In this example, the areas of coordinates A, B, C, and F are infinite - while not shown here, their areas
 * extend forever outside the visible grid. However, the areas of coordinates D and E are finite:
 * D is closest to 9 locations, and E is closest to 17 (both including the coordinate's location itself).
 * Therefore, in this example, the size of the largest area is 17.
 *
 * What is the size of the largest area that isn't infinite?
 *
 * <h2>Part 2</h2>
 * On the other hand, if the coordinates are safe, maybe the best you can do is try to find a region
 * near as many coordinates as possible.
 *
 * For example, suppose you want the sum of the Manhattan distance to all of the coordinates to be
 * less than 32. For each location, add up the distances to all of the given coordinates; if the
 * total of those distances is less than 32, that location is within the desired region. Using the
 * same coordinates as above, the resulting region looks like this:
 * <pre>
 *     ..........
 *     .A........
 *     ..........
 *     ...###..C.
 *     ..#D###...
 *     ..###E#...
 *     .B.###....
 *     ..........
 *     ..........
 *     ........F.
 * </pre>
 *
 * In particular, consider the highlighted location 4,3 located at the top middle of the region.
 * Its calculation is as follows, where abs() is the absolute value function:
 *
 *  - Distance to coordinate A: abs(4-1) + abs(3-1) =  5
 *  - Distance to coordinate B: abs(4-1) + abs(3-6) =  6
 *  - Distance to coordinate C: abs(4-8) + abs(3-3) =  4
 *  - Distance to coordinate D: abs(4-3) + abs(3-4) =  2
 *  - Distance to coordinate E: abs(4-5) + abs(3-5) =  3
 *  - Distance to coordinate F: abs(4-8) + abs(3-9) = 10
 *  - Total distance: 5 + 6 + 4 + 2 + 3 + 10 = 30
 *
 * Because the total distance to all coordinates (30) is less than 32, the location is within the region.
 *
 * This region, which also includes coordinates D and E, has a total size of 16.
 *
 * Your actual region will need to be much larger than this example, though, instead including all
 * locations with a total distance of less than 10000.
 *
 * What is the size of the region containing all locations which have a total distance to all given
 * coordinates of less than 10000?
 *
 * <b>NOTE: </b> Clarified requirements about size of grid by reviewing
 * <a href="https://www.reddit.com/user/TroZShack">Reddit user TroZShack's answer</a> on
 * <a href="https://www.reddit.com/r/adventofcode/comments/a3kr4r/2018_day_6_solutions/">Reddit</a>.
 */
public class Day06 implements Solution<List<Point>> {

    private static final int MAX_XY_ID = -1138;

    @Override
    public List<Point> getInput() throws IOException {
        return toPoints(readLines("/2018/input06.txt"));
    }

    @Override
    public Object solvePart1() throws Exception {
        return findLargestArea(getInput());
    }

    @Override
    public Object solvePart2() throws Exception {
        return findSafestArea(getInput(), 10000);
    }

    public int findSafestArea(final List<Point> points, final int threshhold) {
        final Map<Integer, Point> idMap = buildIdMap(points);
        final Point fakeMaxPoint = idMap.remove(MAX_XY_ID);
        final int maxX = fakeMaxPoint.x, maxY = fakeMaxPoint.y;

        int area = 0;
        for (int x = 0; x <= maxX; x++) {
            for (int y = 0; y <= maxY; y++) {
                int size = 0;
                for (final Point pt : points)
                    size += Point.MANHATTAN_DISTANCE.apply(pt, new Point(x, y));

                if (size < threshhold)
                    area++;
            }
        }

        return area;
    }

    public int findLargestArea(final List<Point> points) {
        final Map<Integer, Point> idMap = buildIdMap(points);
        final Point fakeMaxPoint = idMap.remove(MAX_XY_ID);
        final int maxX = fakeMaxPoint.x, maxY = fakeMaxPoint.y;
        final int[][] grid = new int[maxX + 1][maxY + 1];
        final Map<Integer, Integer> regions = new HashMap<>();

        for (int x = 0; x <= maxX; x++) {
            for (int y = 0; y <= maxY; y++) {
                int closest = maxX + maxY;
                int targetId = -1;

                for (int id : idMap.keySet()) {
                    final Point pt = idMap.get(id);
                    final int dist = Point.MANHATTAN_DISTANCE.apply(pt, new Point(x, y));
                    if (dist < closest) {
                        closest = dist;
                        targetId = id;
                    } else if (dist == closest) {
                        targetId = -1;
                    }
                }

                grid[x][y] = targetId;
                final Integer tally = regions.getOrDefault(targetId, 0);
                regions.put(targetId, 1 + tally);
            }
        }

        // remove infinite regions
        for (int x = 0; x <= maxX; x++) {
            int malo = grid[x][0];
            regions.remove(malo);
            malo = grid[x][maxY];
            regions.remove(malo);
        }
        for (int y = 0; y <= maxY; y++) {
            int malo = grid[0][y];
            regions.remove(malo);
            malo = grid[maxX][y];
            regions.remove(malo);
        }

        return regions.values().stream().max(Comparator.naturalOrder()).orElse(MAX_XY_ID);
    }

    Map<Integer, Point> buildIdMap(final List<Point> points) {
        final Map<Integer, Point> idMap = new HashMap<>();
        int maxX = 0, maxY = 0;

        for (int id = 0, len = points.size(); id < len; id++) {
            final Point pt = points.get(id);
            idMap.put(id, pt);
            if (pt.x > maxX)
                maxX = pt.x;
            if (pt.y > maxY)
                maxY = pt.y;
        }

        // tuck max values inside an invalid ID to be pulled out later... yes, we're cheating to avoid processing twice
        idMap.put(MAX_XY_ID, new Point(maxX, maxY));
        return idMap;
    }

    List<Point> toPoints(final List<String> input) {
        return input.stream()
                .map(str -> Arrays.asList(str.split(",\\s+")))
                .map(lst -> lst.stream().mapToInt(Integer::parseInt).toArray())
                .map(arr -> new Point(arr[0], arr[1]))
                .collect(Collectors.toList());
    }
}
