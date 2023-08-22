package org.base.advent.code2015;

import org.base.advent.util.PermIterator;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <a href="https://adventofcode.com/2015/day/09">Day 09</a>
 */
public class Day09 implements Function<List<String>, Day09.PathDistances> {
    private static final Pattern parser = Pattern.compile("(.+)\\sto\\s(.+)\\s=\\s(\\d+)", Pattern.DOTALL);

    public record PathDistances(int shortest, int longest) {}

    record SantaSleigh(String[] locations, Map<String, Integer> jumpDistanceMap) {}

    @Override
    public PathDistances apply(final List<String> directions) {
        final SantaSleigh sleigh = buildSleigh(directions);
        final PermIterator<String> permIterator = new PermIterator<>(sleigh.locations);

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (final List<String> perm : permIterator) {
            int dist = calculateDistance(perm, sleigh.jumpDistanceMap);
            min = Math.min(min, dist);
            max = Math.max(max, dist);
        }

        return new PathDistances(min, max);
    }

    int calculateDistance(final List<String> path, final Map<String, Integer> jumpDistanceMap) {
        int dist = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            final String key = key(path.get(i), path.get(i + 1));
            final Integer step = jumpDistanceMap.get(key);
            if (step == null)
                throw new NullPointerException(key);
            else
                dist += step;
        }

        return dist;
    }

    private String key(final String loc1, final String loc2) {
        return "JUMP-"+ Arrays.asList(loc1, loc2);
    }

    SantaSleigh buildSleigh(final List<String> directions) {
        final Set<String> locations = new HashSet<>();
        final Map<String, Integer> distanceMap = new HashMap<>();

        for (final String directive : directions) {
            final Matcher matcher = parser.matcher(directive);
            if (matcher.matches()) {
                final String city1 = matcher.group(1);
                final String city2 = matcher.group(2);
                final Integer distance = Integer.parseInt(matcher.group(3));
                locations.add(city1);
                locations.add(city2);
                distanceMap.put(key(city1, city2), distance);
                distanceMap.put(key(city2, city1), distance);
            }
            else {
                throw new RuntimeException("No match: "+ directive);
            }
        }

        return new SantaSleigh(locations.toArray(new String[0]), distanceMap);
    }
}
