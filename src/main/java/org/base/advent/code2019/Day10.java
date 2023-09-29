package org.base.advent.code2019;

import org.base.advent.util.Point;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <a href="https://adventofcode.com/2019/day/10">Day 10</a>
 */
public class Day10 implements Function<List<String>, Day10.Asteroids> {
    public record Asteroids(int max, long vaporize) {}

    @Override
    public Asteroids apply(List<String> input) {
        final Map<Point, Integer> asteroids = mapVisibleAsteroids(input);
        final int maxAsteroids = maxAsteroids(asteroids);
        final Point _200th = vaporize(asteroids);
        return new Asteroids(maxAsteroids, _200th.x * 100 + _200th.y);
    }

    int maxAsteroids(final Map<Point, Integer> asteroids) {
        return asteroids.values().stream().max(Comparator.naturalOrder()).orElse(-1);
    }

    Point vaporize(final Map<Point, Integer> asteroids) {
        final Point deathStar = asteroids.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue)).orElseThrow().getKey();
        final List<Point> vaporized = new ArrayList<>();

        while (vaporized.size() < 200) {
            final Map<Double, Point> visible = countVisibleAsteroids(deathStar, asteroids.keySet());
            for (double rad : visible.keySet()) {
                final Point gone = visible.get(rad);
                vaporized.add(gone);
                asteroids.remove(gone);
            }
        }

        return vaporized.get(199);
    }

    Map<Point, Integer> mapVisibleAsteroids(final List<String> rows) {
        final Set<Point> asteroids = scanAsteroids(rows);
        return asteroids.stream().collect(Collectors.toMap(
                Function.identity(), (Point pt) -> countVisibleAsteroids(pt, asteroids).size()));
    }

    Map<Double, Point> countVisibleAsteroids(final Point asteroid, final Set<Point> space) {
        final List<Point> graph = new ArrayList<>(space);
        graph.remove(asteroid);
        graph.sort(Comparator.comparing(a -> distance(a, asteroid)));
        final Map<Double, Point> angles = new TreeMap<>();
        for (final Point point : graph) {
            final double angle = angle(asteroid, point);
            if (!angles.containsKey(angle)) angles.put(angle, point);
        }

        return angles;
    }

    Set<Point> scanAsteroids(final List<String> rows) {
        final Set<Point> asteroids = new LinkedHashSet<>();
        for (int y = 0; y < rows.size(); y++) {
            final String row = rows.get(y);
            for (int x = 0; x < row.length(); x++)
                if (row.charAt(x) == '#')
                    asteroids.add(Point.of(x, y));
        }

        return asteroids;
    }

    double angle(final Point pt, final Point target) {
        // starts pointing up when cartesian graph is upside down
        double angle = Math.toDegrees(Math.atan2(target.y - pt.y, target.x - pt.x)) + 90.0d;
        return (angle < 0) ? angle + 360 : angle;
    }

    public BigDecimal distance(final Point from, final Point point) {
        final double dist = Math.hypot(Math.abs(point.y - from.y), Math.abs(point.x - from.x));
        return BigDecimal.valueOf(dist);
    }
}
