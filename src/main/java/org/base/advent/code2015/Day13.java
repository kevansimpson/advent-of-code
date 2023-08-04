package org.base.advent.code2015;

import lombok.Getter;
import org.base.advent.Solution;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <a href="https://adventofcode.com/2015/day/13">Day 13</a>
 */
public class Day13 implements Solution<List<String>> {
    private static final String ME = "ME";

    //Alice would gain 2 happiness units by sitting next to Bob.
    //Alice would gain 26 happiness units by sitting next to Carol.
    private static final Pattern parser = Pattern.compile(
            "(.+)\\swould\\s(.+)\\s(\\d+).*to\\s(.+)\\.", Pattern.DOTALL);

    private final Set<String> people = new HashSet<>();
    private final Map<List<String>, Integer> distanceMap = new HashMap<>();
    private Map<String, Integer> jumpDistanceMap;

    @Getter
    private final List<String> input = readLines("/2015/input13.txt");

    @Override
    public Object solvePart1() {
        return solveOptimal(getInput());
    }

    @Override
    public Object solvePart2() {
        return solveWithMe(getInput());
    }

    public int solveOptimal(final List<String> directions) {
        jumpDistanceMap = buildDistanceMap(directions);
        
        final List<String> permutation = new ArrayList<>();
        final List<String> peopleList = new ArrayList<>(this.people);
        buildAllPaths(peopleList, permutation, perm -> distanceMap.put(perm, calculateDistance(perm)));
        
        int longest = Integer.MIN_VALUE;
        List<String> optimal = null;
        for (final List<String> path : distanceMap.keySet()) {
            final Integer dist = distanceMap.get(path);
            if (dist > longest) {
                longest = dist;
                optimal = path;
            }
        }
        
        debug("The optimal seating arrangement is: "+ optimal);
        return longest;
    }

    public int solveWithMe(final List<String> directions) {
        distanceMap.clear();
        jumpDistanceMap.clear();
        people.clear();
        people.add(ME);
        return solveOptimal(directions);
    }

    protected int calculateDistance(final List<String> path) {
        final int last = path.size() - 1;
        int dist = getDelta(path.get(last), path.get(0));
        dist += getDelta(path.get(0), path.get(last));
        for (int i = 0; i < last; i++) {
            final String person1 = path.get(i);
            final String person2 = path.get(i + 1);
            dist += getDelta(person1, person2);
            dist += getDelta(person2, person1);
        }

        return dist;
    }

    protected int getDelta(final String person1, final String person2) {
        if (Arrays.asList(person1, person2).contains(ME))
            return 0;

        final String key = key(person1, person2);
        final Integer step = jumpDistanceMap.get(key);
        if (step == null)
            throw new NullPointerException(key);
        else
            return step;
    }

    protected String key(final String loc1, final String loc2) {
        return "JUMP-"+ Arrays.asList(loc1, loc2);
    }

    protected Map<String, Integer> buildDistanceMap(final List<String> directions) {
        final Map<String, Integer> distanceMap = new HashMap<>();
        for (final String directive : directions) {
            final Matcher matcher = parser.matcher(directive);
            if (matcher.matches()) {
                //Alice would gain 2 happiness units by sitting next to Bob.
                //Alice would gain 26 happiness units by sitting next to Carol.
                final String person1 = matcher.group(1), person2 = matcher.group(4);
                final int distance = Integer.parseInt(matcher.group(3));
                people.add(person1);
                people.add(person2);
                distanceMap.put(key(person1, person2), (distance * ("gain".equals(matcher.group(2)) ? 1 : -1)));
            }
            else {
                throw new RuntimeException("No match: "+ directive);
            }
        }

        return distanceMap;
    }
}
