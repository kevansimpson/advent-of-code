package org.base.advent.code2015;

import org.base.advent.util.PermIterator;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <a href="https://adventofcode.com/2015/day/13">Day 13</a>
 */
public class Day13 implements Function<List<String>, Day13.OptimalHappiness> {
    private static final String ME = "ME";
    private static final Pattern parser = Pattern.compile(
            "(.+)\\swould\\s(.+)\\s(\\d+).*to\\s(.+)\\.", Pattern.DOTALL);

    public record OptimalHappiness(int withoutMe, int withMe) {}
    record RoundTable(Set<String> people, Map<String, Integer> distanceMap) {}

    @Override
    public OptimalHappiness apply(List<String> input) {
        RoundTable roundTable = buildRoundTable(input);
        int withoutMe = solveOptimal(roundTable);
        roundTable.people.add(ME);
        return new OptimalHappiness(withoutMe, solveOptimal(roundTable));
    }

    int solveOptimal(final RoundTable roundTable) {
        final PermIterator<String> permIterator = new PermIterator<>(roundTable.people.toArray(new String[0]));
        int optimal = 0;
        for (final List<String> perm : permIterator) {
            optimal = Math.max(optimal, calculateDistance(perm, roundTable.distanceMap));
        }
        return optimal;
    }

    int calculateDistance(final List<String> path, final Map<String, Integer> distanceMap) {
        final int last = path.size() - 1;
        int dist = getDelta(path.get(last), path.get(0), distanceMap);
        dist += getDelta(path.get(0), path.get(last), distanceMap);
        for (int i = 0; i < last; i++) {
            final String person1 = path.get(i);
            final String person2 = path.get(i + 1);
            dist += getDelta(person1, person2, distanceMap);
            dist += getDelta(person2, person1, distanceMap);
        }

        return dist;
    }

    int getDelta(final String person1, final String person2, final Map<String, Integer> distanceMap) {
        if (ME.equals(person1) || ME.equals(person2))
            return 0;

        return distanceMap.get(key(person1, person2));
    }

    String key(final String loc1, final String loc2) {
        return "JUMP-"+ loc1 + loc2;
    }

    RoundTable buildRoundTable(final List<String> directions) {
        final Set<String> people = new HashSet<>();
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

        return new RoundTable(people, distanceMap);
    }
}
