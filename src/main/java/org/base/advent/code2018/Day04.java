package org.base.advent.code2018;

import lombok.Data;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringUtils;
import org.base.advent.Solution;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * <h2>Part 1</h2>
 * You've sneaked into another supply closet - this time, it's across from the prototype suit
 * manufacturing lab. You need to sneak inside and fix the issues with the suit, but there's
 * a guard stationed outside the lab, so this is as close as you can safely get.
 *
 * As you search the closet for anything that might help, you discover that you're not the first
 * person to want to sneak in. Covering the walls, someone has spent an hour starting every
 * midnight for the past few months secretly observing this guard post! They've been writing down
 * the ID of the one guard on duty that night - the Elves seem to have decided that one guard was
 * enough for the overnight shift - as well as when they fall asleep or wake up while at their post (your puzzle input).
 *
 * For example, consider the following records, which have already been organized into chronological order:
 * <pre>
 *     [1518-11-01 00:00] Guard #10 begins shift
 *     [1518-11-01 00:05] falls asleep
 *     [1518-11-01 00:25] wakes up
 *     [1518-11-01 00:30] falls asleep
 *     [1518-11-01 00:55] wakes up
 *     [1518-11-01 23:58] Guard #99 begins shift
 *     [1518-11-02 00:40] falls asleep
 *     [1518-11-02 00:50] wakes up
 *     [1518-11-03 00:05] Guard #10 begins shift
 *     [1518-11-03 00:24] falls asleep
 *     [1518-11-03 00:29] wakes up
 *     [1518-11-04 00:02] Guard #99 begins shift
 *     [1518-11-04 00:36] falls asleep
 *     [1518-11-04 00:46] wakes up
 *     [1518-11-05 00:03] Guard #99 begins shift
 *     [1518-11-05 00:45] falls asleep
 *     [1518-11-05 00:55] wakes up
 * </pre>
 *
 * Timestamps are written using year-month-day hour:minute format. The guard falling asleep or waking up
 * is always the one whose shift most recently started. Because all asleep/awake times are during the
 * midnight hour (00:00 - 00:59), only the minute portion (00 - 59) is relevant for those events.
 *
 * Visually, these records show that the guards are asleep at these times:
 * <pre>
 *     Date   ID   Minute
 *                000000000011111111112222222222333333333344444444445555555555
 *                012345678901234567890123456789012345678901234567890123456789
 *    11-01  #10  .....####################.....#########################.....
 *    11-02  #99  ........................................##########..........
 *    11-03  #10  ........................#####...............................
 *    11-04  #99  ....................................##########..............
 *    11-05  #99  .............................................##########.....
 * </pre>
 *
 * The columns are Date, which shows the month-day portion of the relevant day;
 *                 ID, which shows the guard on duty that day; and
 *                 Minute, which shows the minutes during which the guard was asleep within the midnight hour.
 * (The Minute column's header shows the minute's ten's digit in the first row and the one's digit in the second row.)
 * Awake is shown as ., and asleep is shown as #.
 *
 * Note that guards count as asleep on the minute they fall asleep, and they count as awake on the minute
 * they wake up. For example, because Guard #10 wakes up at 00:25 on 1518-11-01, minute 25 is marked as awake.
 *
 * If you can figure out the guard most likely to be asleep at a specific time, you might be able to trick that
 * guard into working tonight so you can have the best chance of sneaking in. You have two strategies for
 * choosing the best guard/minute combination.
 *
 * <b>Strategy 1:</b> Find the guard that has the most minutes asleep. What minute does that guard spend asleep
 * the most?
 *
 * In the example above, Guard #10 spent the most minutes asleep, a total of 50 minutes (20+25+5), while
 * Guard #99 only slept for a total of 30 minutes (10+10+10). Guard #10 was asleep most during minute 24
 * (on two days, whereas any other minute the guard was asleep was only seen on one day).
 *
 * While this example listed the entries in chronological order, your entries are in the order you found them.
 * You'll need to organize them before they can be analyzed.
 *
 * What is the ID of the guard you chose multiplied by the minute you chose?
 * (In the above example, the answer would be 10 * 24 = 240.)
 *
 * <h2>Part 2</h2>
 * Strategy 2: Of all guards, which guard is most frequently asleep on the same minute?
 *
 * In the example above, Guard #99 spent minute 45 asleep more than any other guard or minute -
 * three times in total. (In all other cases, any guard spent any minute asleep at most twice.)
 *
 * What is the ID of the guard you chose multiplied by the minute you chose?
 * (In the above example, the answer would be 99 * 45 = 4455.)
 */
public class Day04 implements Solution<List<String>> {

    @Override
    public List<String> getInput() throws IOException {
        final List<String> lines = readLines("/2018/input04.txt");
        Collections.sort(lines);
        return lines;
    }

    @Override
    public Object solvePart1() throws Exception {
        return strategy1(getInput());
    }

    @Override
    public Object solvePart2() throws Exception {
        return strategy2(getInput());
    }

    public int strategy1(final List<String> input) {
        final Guard sleepy = findSleepiestGuard(input);
        return sleepy.getId() * sleepy.getSleepiestMinute();
    }

    public int strategy2(final List<String> input) {
        final Map<Integer, Guard> guardMap = parseRecords(input);
        final Map<Guard, Integer> sleepiest = guardMap.values().stream()
                .collect(Collectors.toMap(g -> g, Guard::getSleepiestMinute));
        int maxSlept = -1;
        Guard sleepyGuard = null;

        for (Map.Entry<Guard, Integer> entry : sleepiest.entrySet()) {
            final Guard guard = entry.getKey();
            if (guard.getRanges().isEmpty()) continue;

            final int slept = guard.getSleepTallies()[entry.getValue()];
            if (slept > maxSlept) {
                maxSlept = slept;
                sleepyGuard = guard;
            }
        }

        return sleepyGuard.getId() * sleepiest.get(sleepyGuard);
    }

    Guard findSleepiestGuard(final List<String> input) {
        final Map<Integer, Guard> guardMap = parseRecords(input);
//        guardMap.entrySet().forEach(entry -> {
//            System.out.println(entry.getValue());
//            System.out.println(entry.getValue().getTimeAsleep());
//            entry.getValue().getRanges().entrySet().forEach(System.out::println);
//        });

        final Optional<Guard> optional = guardMap.values().stream()
                .sorted(Comparator.comparingInt(Guard::getTimeAsleep).reversed()).findFirst();
        return optional.orElse(null);
    }

    private static final Pattern REGEX = Pattern.compile(
            "^\\[(\\d{4})\\-(\\d{2})\\-(\\d{2})\\s(\\d{2}):(\\d{2})\\]\\s(.+)$");

    Map<Integer, Guard> parseRecords(final List<String> input) {
//        input.forEach(System.out::println);
        final Map<Integer, Guard> records = new LinkedHashMap<>();
        Guard current = null;
        int falls = -1, wakes;

        for (String rec : input) {
            final Matcher matcher = REGEX.matcher(rec);
            if (matcher.matches()) {
                final int day = Integer.parseInt(matcher.group(3));
                final String[] words = matcher.group(6).split("\\s");

                switch (words[0]) {
                    case "Guard":
                        final int id = Integer.parseInt(StringUtils.remove(words[1], "#"));
                        current = records.getOrDefault(id, new Guard(id));
                        records.put(id, current);
                        break;
                    case "falls":
                        falls = Integer.parseInt(matcher.group(5));
                        final List<Range<Integer>> rangeList =
                                current.getRanges().getOrDefault(day, new ArrayList<>());
                        current.getRanges().put(day, rangeList);
                        break;
                    case "wakes":
                        wakes = Integer.parseInt(matcher.group(5));
                        current.getRanges().get(day).add(Range.between(falls, wakes));
                        break;
                    default:
                        throw new RuntimeException(rec);
                }
            }
            else
                throw new IllegalArgumentException(rec);
        }

        return records;
    }

    @Data
    public static class Guard {
        private int id;
                   // day      falls -> wakes
        private Map<Integer, List<Range<Integer>>> ranges = new LinkedHashMap<>();

        public Guard(final int newId) {
            id = newId;
        }

        public int getTimeAsleep() {
            return getRanges().values().stream()
                    .flatMap(l -> l.stream())
                    .mapToInt(range -> (range.getMaximum() - range.getMinimum()))
                    .sum();
        }

        public int[] getSleepTallies() {
            final List<Range<Integer>> rangeList = getRanges().values().stream()
                    .flatMap(l -> l.stream()).collect(Collectors.toList());

            final int[] counts = new int[60];
            for (final Range<Integer> range : rangeList) {
                for (int i = range.getMinimum(); i < range.getMaximum(); i++)
                    counts[i] += 1;
            }

            return counts;
        }

        public int getSleepiestMinute() {
            final int[] counts = getSleepTallies();
            int largest = 0;
            for ( int i = 1; i < counts.length; i++ )
                if ( counts[i] > counts[largest] ) largest = i;

            return largest; // position of the first largest found
        }
    }
}
