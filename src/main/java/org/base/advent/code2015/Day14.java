package org.base.advent.code2015;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.base.advent.Solution;

/**
 * <h2>Part 1</h2>
 * This year is the Reindeer Olympics! Reindeer can fly at high speeds, but must rest occasionally to recover
 * their energy. Santa would like to know which of his reindeer is fastest, and so he has them race.
 *
 * Reindeer can only either be flying (always at their top speed) or resting (not moving at all), and always
 * spend whole seconds in either state.
 *
 * For example, suppose you have the following Reindeer:
 *
 *  - Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
 *  - Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.
 * After one second, Comet has gone 14 km, while Dancer has gone 16 km. After ten seconds, Comet has gone 140 km, while
 * Dancer has gone 160 km. On the eleventh second, Comet begins resting (staying at 140 km), and Dancer continues on
 * for a total distance of 176 km. On the 12th second, both reindeer are resting. They continue to rest until the
 * 138th second, when Comet flies for another ten seconds. On the 174th second, Dancer flies for another 11 seconds.
 *
 * In this example, after the 1000th second, both reindeer are resting, and Comet is in the lead at 1120 km (poor Dancer
 * has only gotten 1056 km by that point). So, in this situation, Comet would win (if the race ended at 1000 seconds).
 *
 * Given the descriptions of each reindeer (in your puzzle input), after exactly 2503 seconds, what distance has the
 * winning reindeer traveled?
 *
 * <h2>Part 2</h2>
 * Seeing how reindeer move in bursts, Santa decides he's not pleased with the old scoring system.
 *
 * Instead, at the end of each second, he awards one point to the reindeer currently in the lead. (If there are
 * multiple reindeer tied for the lead, they each get one point.) He keeps the traditional 2503 second time limit,
 * of course, as doing otherwise would be entirely ridiculous.
 *
 * Given the example reindeer from above, after the first second, Dancer is in the lead and gets one point. He stays
 * in the lead until several seconds into Comet's second burst: after the 140th second, Comet pulls into the lead and
 * gets his first point. Of course, since Dancer had been in the lead for the 139 seconds before that, he has
 * accumulated 139 points by the 140th second.
 *
 * After the 1000th second, Dancer has accumulated 689 points, while poor Comet, our old champion, only has 312. So,
 * with the new scoring system, Dancer would win (if the race ended at 1000 seconds).
 *
 * Again given the descriptions of each reindeer (in your puzzle input), after exactly 2503 seconds, how many points
 * does the winning reindeer have?
 *
 */
public class Day14 implements Solution<List<String>> {

    @Override
    public List<String> getInput() throws IOException {
        return readLines("/2015/input14.txt");
    }

    @Override
    public Object solvePart1() throws Exception {
        return distanceTraveled(getInput());
    }

    @Override
    public Object solvePart2() throws Exception {
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

    
    private static Pattern parser = Pattern.compile(
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
