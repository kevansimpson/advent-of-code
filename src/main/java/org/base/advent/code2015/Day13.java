package org.base.advent.code2015;

import org.base.advent.Solution;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h2>Part 1</h2>
 * In years past, the holiday feast with your family hasn't gone so well. Not everyone gets along! This year, you
 * resolve, will be different. You're going to find the optimal seating arrangement and avoid all those awkward
 * conversations.
 *
 * You start by writing up a list of everyone invited and the amount their happiness would increase or decrease if
 * they were to find themselves sitting next to each other person. You have a circular table that will be just big
 * enough to fit everyone comfortably, and so each person will have exactly two neighbors.
 *
 * For example, suppose you have only four attendees planned, and you calculate their potential happiness as follows:
 *
 * Alice would gain 54 happiness units by sitting next to Bob.
 * Alice would lose 79 happiness units by sitting next to Carol.
 * Alice would lose 2 happiness units by sitting next to David.
 * Bob would gain 83 happiness units by sitting next to Alice.
 * Bob would lose 7 happiness units by sitting next to Carol.
 * Bob would lose 63 happiness units by sitting next to David.
 * Carol would lose 62 happiness units by sitting next to Alice.
 * Carol would gain 60 happiness units by sitting next to Bob.
 * Carol would gain 55 happiness units by sitting next to David.
 * David would gain 46 happiness units by sitting next to Alice.
 * David would lose 7 happiness units by sitting next to Bob.
 * David would gain 41 happiness units by sitting next to Carol.
 *
 * Then, if you seat Alice next to David, Alice would lose 2 happiness units (because David talks so much), but
 * David would gain 46 happiness units (because Alice is such a good listener), for a total change of 44.
 *
 * If you continue around the table, you could then seat Bob next to Alice (Bob gains 83, Alice gains 54). Finally,
 * seat Carol, who sits next to Bob (Carol gains 60, Bob loses 7) and David (Carol gains 55, David gains 41). The
 * arrangement looks like this:
 *
 *        +41 +46
 *  +55    David     -2
 * Carol          Alice
 *  +60     Bob     +54
 *        -7  +83
 *
 * After trying every other seating arrangement in this hypothetical scenario, you find that this one is the most
 * optimal, with a total change in happiness of 330.
 *
 * What is the total change in happiness for the optimal seating arrangement of the actual guest list?
 *
 * <h2>Part 2</h2>
 * In all the commotion, you realize that you forgot to seat yourself. At this point, you're pretty apathetic toward
 * the whole thing, and your happiness wouldn't really go up or down regardless of who you sit next to. You assume
 * everyone else would be just as ambivalent about sitting next to you, too.
 *
 * So, add yourself to the list, and give all happiness relationships that involve you a score of 0.
 *
 * What is the total change in happiness for the optimal seating arrangement that actually includes yourself?
 *
 */
public class Day13 implements Solution<List<String>> {
    private static final String ME = "ME";

    //Alice would gain 2 happiness units by sitting next to Bob.
    //Alice would gain 26 happiness units by sitting next to Carol.
    private static Pattern parser = Pattern.compile(
            "(.+)\\swould\\s(.+)\\s(\\d+).*to\\s(.+)\\.", Pattern.DOTALL);

    private Set<String> people = new HashSet<>();
    private Map<List<String>, Integer> distanceMap = new HashMap<>();
    private Map<String, Integer> jumpDistanceMap;


    @Override
    public List<String> getInput() throws IOException {
        return readLines("/2015/input13.txt");
    }

    @Override
    public Object solvePart1() throws Exception {
        return solveOptimal(getInput());
    }

    @Override
    public Object solvePart2() throws Exception {
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
        return "JUMP-"+ Arrays.asList(loc1, loc2).toString();
    }

    protected Map<String, Integer> buildDistanceMap(final List<String> directions) {
        final Map<String, Integer> distanceMap = new HashMap<>();
        for (final String directive : directions) {
            final Matcher matcher = parser.matcher(directive);
            if (matcher.matches()) {
                //Alice would gain 2 happiness units by sitting next to Bob.
                //Alice would gain 26 happiness units by sitting next to Carol.
                final String person1 = matcher.group(1), person2 = matcher.group(4);
                final Integer distance = Integer.parseInt(matcher.group(3));
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
