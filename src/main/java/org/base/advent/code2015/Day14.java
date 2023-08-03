package org.base.advent.code2015;

import org.base.advent.Solution;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <a href="https://adventofcode.com/2015/day/14">Day 14</a>
 */
public class Day14 implements Solution<List<String>> {

    @Override
    public List<String> getInput(){
        return readLines("/2015/input14.txt");
    }

    @Override
    public Object solvePart1() {
        return distanceTraveled(getInput());
    }

    @Override
    public Object solvePart2() {
        return winningPoints(getInput());
    }


    public int distanceTraveled(final List<String> input) {
        final Map<String, ReindeerSpeed> speedMap = buildSpeedMap(input);
        final TreeMap<Integer, List<String>> distanceMap = buildDistanceMap(speedMap, 2503);

        return distanceMap.lastKey();
    }

    public int winningPoints(final List<String> input) {
        final Map<String, ReindeerSpeed> speedMap = buildSpeedMap(input);
        final Map<String, String> pointMap = new HashMap<>();

        speedMap.keySet().forEach(reindeer -> pointMap.put(reindeer, ""));

        for (int i = 1; i < 2504; i++) {
            final List<String> winners = identifyWinner(speedMap, i);
            winners.forEach(w -> pointMap.put(w, pointMap.get(w).concat(".")));
        }

        int highest = 0;
        String winner = null;
        for (final String reindeer : pointMap.keySet()) {
            final int points = pointMap.get(reindeer).length();
            if (points > highest) {
                winner = reindeer;
                highest = points;
            }
        }

        // > 1075 - didn't account for ties
        debug("The winning reindeer is "+ winner);
        return highest;
    }

    protected List<String> identifyWinner(final Map<String, ReindeerSpeed> speedMap, final int seconds) {
        final TreeMap<Integer, List<String>> distanceMap = buildDistanceMap(speedMap, seconds);
        return distanceMap.get(distanceMap.lastKey());
    }

    /* Snapshot */
    protected TreeMap<Integer, List<String>> buildDistanceMap(final Map<String, ReindeerSpeed> speedMap, final int seconds) {
        final TreeMap<Integer, List<String>> distanceMap = new TreeMap<>();
        for (final String reindeer : speedMap.keySet()) {
            final int dist = calculateDistance(speedMap.get(reindeer), seconds);
            if (!distanceMap.containsKey(dist)) {
                distanceMap.put(dist, new ArrayList<>());
            }
            distanceMap.get(dist).add(reindeer);
        }

        return distanceMap;
    }

    protected int calculateDistance(final ReindeerSpeed speed, final int seconds) {
        final int totalTime = speed.goTime + speed.restTime;
        return ((seconds / totalTime) * speed.goTime * speed.kmPerSec +
                (Math.min((seconds % totalTime), speed.goTime) * speed.kmPerSec));
    }

    
    private static final Pattern parser = Pattern.compile(
            "(.+)\\scan.+\\s(\\d+)\\skm.+\\s(\\d+)\\sseconds.*\\s(\\d+)\\s.+", Pattern.DOTALL);

    protected Map<String, ReindeerSpeed> buildSpeedMap(final List<String> speeds) {
        final Map<String, ReindeerSpeed> speedMap = new HashMap<>();
        for (final String directive : speeds) {
            final Matcher matcher = parser.matcher(directive);
            if (matcher.matches()) {
                final int kmPerSec = Integer.parseInt(matcher.group(2));
                final int goTime = Integer.parseInt(matcher.group(3));
                final int restTime = Integer.parseInt(matcher.group(4));
                speedMap.put(matcher.group(1), new ReindeerSpeed(kmPerSec, goTime, restTime));
            }
            else {
                throw new RuntimeException("No match: "+ directive);
            }
        }

        return speedMap;
    }

    private static class ReindeerSpeed {
        public final int kmPerSec, goTime, restTime;
        public ReindeerSpeed(final int velocity, final int go, final int rest) {
            this.kmPerSec = velocity;
            this.goTime = go;
            this.restTime = rest;
        }
        
        @Override
        public String toString() {
            return Arrays.asList(kmPerSec, goTime, restTime).toString();
        }
    }
}
