package org.base.advent.code2015;

import lombok.Getter;
import org.base.advent.Solution;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <a href="https://adventofcode.com/2015/day/09">Day 09</a>
 */
@SuppressWarnings("OptionalGetWithoutIsPresent")
public class Day09 implements Solution<List<String>> {
    private static final Pattern parser = Pattern.compile("(.+)\\sto\\s(.+)\\s=\\s(\\d+)", Pattern.DOTALL);

    private final Set<String> locations = new HashSet<>();
    private final Map<List<String>, Integer> distanceMap = new HashMap<>();
    private Map<String, Integer> jumpDistanceMap;

    @Getter
    private final List<String> input = readLines("/2015/input09.txt");

    @Override
    public Object solvePart1() {
        return shortestPath(getInput());
    }

    @Override
    public Object solvePart2() {
        return longestPath();
    }

    public int shortestPath(final List<String> directions) {
        jumpDistanceMap = buildDistanceMap(directions);
        
        final List<String> permutation = new ArrayList<>();
        final List<String> locationList = new ArrayList<>(this.locations);

        buildAllPaths(locationList, permutation, perm -> distanceMap.put(perm, calculateDistance(perm)));

        return distanceMap.values().stream().min(Comparator.naturalOrder()).get();
    }

    public int longestPath() {
        return distanceMap.values().stream().max(Comparator.naturalOrder()).get();
    }

    int calculateDistance(final List<String> path) {
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

    protected Map<String, Integer> buildDistanceMap(final List<String> directions) {
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

        return distanceMap;
    }
}
