package org.base.advent.code2015;

import org.base.advent.Solution;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h2>Part 1</h2>
 * Every year, Santa manages to deliver all of his presents in a single night.
 *
 * This year, however, he has some new locations to visit; his elves have provided him the distances between every
 * pair of locations. He can start and end at any two (different) locations he wants, but he must visit each location
 * exactly once. What is the shortest distance he can travel to achieve this?
 *
 * For example, given the following distances:
 *
 * London to Dublin = 464
 * London to Belfast = 518
 * Dublin to Belfast = 141
 *
 * The possible routes are therefore:
 * Dublin -> London -> Belfast = 982
 * London -> Dublin -> Belfast = 605
 * London -> Belfast -> Dublin = 659
 * Dublin -> Belfast -> London = 659
 * Belfast -> Dublin -> London = 605
 * Belfast -> London -> Dublin = 982
 *
 * The shortest of these is London -> Dublin -> Belfast = 605, and so the answer is 605 in this example.
 *
 * What is the distance of the shortest route?
 *
 * <h2>Part 2</h2>
 * The next year, just to show off, Santa decides to take the route with the longest distance instead.
 *
 * He can still start and end at any two (different) locations he wants, and he still must visit each location
 * exactly once.
 *
 * For example, given the distances above, the longest route would be 982 via (for example) Dublin -> London -> Belfast.
 *
 * What is the distance of the longest route?
 */
public class Day09 implements Solution<List<String>> {
    private static Pattern parser = Pattern.compile("(.+)\\sto\\s(.+)\\s=\\s(\\d+)", Pattern.DOTALL);

    private Set<String> locations = new HashSet<>();
    private Map<List<String>, Integer> distanceMap = new HashMap<>();
    private Map<String, Integer> jumpDistanceMap;


    @Override
    public List<String> getInput() throws IOException {
        return readLines("/2015/input09.txt");
    }

    @Override
    public Object solvePart1() throws Exception {
        return shortestPath(getInput());
    }

    @Override
    public Object solvePart2() throws Exception {
        return longestPath();
    }

    public int shortestPath(final List<String> directions) {
        jumpDistanceMap = buildDistanceMap(directions);
        
        final List<String> permutation = new ArrayList<>();
        final List<String> locationList = new ArrayList<>(this.locations);

        recurse(locationList, permutation, perm -> distanceMap.put(perm, calculateDistance(perm)));

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
        return "JUMP-"+ Arrays.asList(loc1, loc2).toString();
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
