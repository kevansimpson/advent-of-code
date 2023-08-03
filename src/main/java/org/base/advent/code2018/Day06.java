package org.base.advent.code2018;

import org.base.advent.Solution;
import org.base.advent.util.Point;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <a href="https://adventofcode.com/2018/day/05">Day 05</a>
 * <p>
 * <b>NOTE: </b> Clarified requirements about size of grid by reviewing
 * <a href="https://www.reddit.com/user/TroZShack">Reddit user TroZShack's answer</a> on
 * <a href="https://www.reddit.com/r/adventofcode/comments/a3kr4r/2018_day_6_solutions/">Reddit</a>.
 */
public class Day06 implements Solution<List<Point>> {

    private static final int MAX_XY_ID = -1138;

    @Override
    public List<Point> getInput(){
        return toPoints(readLines("/2018/input06.txt"));
    }

    @Override
    public Object solvePart1() {
        return findLargestArea(getInput());
    }

    @Override
    public Object solvePart2() {
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
