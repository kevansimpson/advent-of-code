package org.base.advent.code2019;

import org.base.advent.util.Point;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <a href="https://adventofcode.com/2019/day/10">Day 10</a>
 */
public class Day10 implements Function<List<String>, Day10.Asteroids> {
    public record Asteroids(int max, long vaporize) {}

    @Override
    public Asteroids apply(List<String> strings) {
        final String[] input = strings.toArray(new String[36]);
        final Point _200th = vaporize(input);
        return new Asteroids(maxAsteroids(input), _200th.x * 100 + _200th.y);
    }

    int maxAsteroids(final String... rows) {
        return mapVisibleAsteroids(rows).values().stream().max(Comparator.naturalOrder()).orElse(-1);
    }

    Point vaporize(final String... rows) {
        final Map<Point, Integer> asteroids = mapVisibleAsteroids(rows);
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

    Map<Point, Integer> mapVisibleAsteroids(final String... rows) {
        final Set<Point> asteroids = scanAsteroids(rows);
        return asteroids.stream().collect(
                Collectors.toMap(pt -> pt, (Point pt) -> countVisibleAsteroids(pt, asteroids).size()));
    }

    /*
.#..#       [1,0]             [4,0]
.....
##### [0,2] [1,2] [2,2] [3,2] [4,2]
....#                         [4,3]
...##                   [3,4] [4,4]
     */
    Map<Double, Point> countVisibleAsteroids(final Point asteroid, final Set<Point> space) {
        final List<Point> graph = new ArrayList<>(space);
        graph.remove(asteroid);
        graph.sort(Comparator.comparing(a -> a.distance(asteroid)));
        final Map<Double, Point> angles = new TreeMap<>();
        for (final Point point : graph) {
            final double angle = asteroid.angle(point);
            if (!angles.containsKey(angle)) angles.put(angle, point);
        }

        return angles;
    }

    Set<Point> scanAsteroids(final String... rows) {
        final Set<Point> asteroids = new LinkedHashSet<>();
        for (int y = 0; y < rows.length; y++) {
            final String row = rows[y];
            for (int x = 0; x < row.length(); x++) {
                if (row.charAt(x) == '#') asteroids.add(Point.of(x, y));
            }
        }

        return asteroids;
    }
}
