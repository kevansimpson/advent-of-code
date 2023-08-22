package org.base.advent.code2018;

import lombok.Data;
import lombok.NonNull;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <a href="https://adventofcode.com/2018/day/04">Day 04</a>
 */
public class Day04 implements Function<List<String>, Day04.GuardStrategy> {
    public record GuardStrategy(int one, int two) {}

    @Override
    public GuardStrategy apply(List<String> lines) {
        Collections.sort(lines);
        return new GuardStrategy(strategy1(lines), strategy2(lines));
    }

    int strategy1(final List<String> input) {
        final Guard sleepy = findSleepiestGuard(input);
        return sleepy.getId() * sleepy.getSleepiestMinute();
    }

    int strategy2(final List<String> input) {
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

        return Objects.requireNonNull(sleepyGuard).getId() * sleepiest.get(sleepyGuard);
    }

    Guard findSleepiestGuard(final List<String> input) {
        return parseRecords(input).values().stream()
                .max(Comparator.comparingInt(Guard::getTimeAsleep)).orElse(null);
    }

    Map<Integer, Guard> parseRecords(final List<String> input) {
        final Pattern REGEX = Pattern.compile(
                "^\\[(\\d{4})-(\\d{2})-(\\d{2})\\s(\\d{2}):(\\d{2})]\\s(.+)$");
        final Map<Integer, Guard> records = new LinkedHashMap<>();
        Guard current = null;
        int falls = -1, wakes;

        for (String rec : input) {
            final Matcher matcher = REGEX.matcher(rec);
            if (matcher.matches()) {
                final int day = Integer.parseInt(matcher.group(3));
                final String[] words = matcher.group(6).split("\\s");

                switch (words[0]) {
                    case "Guard" -> {
                        final int id = Integer.parseInt(StringUtils.remove(words[1], "#"));
                        current = records.getOrDefault(id, new Guard(id));
                        records.put(id, current);
                    }
                    case "falls" -> {
                        falls = Integer.parseInt(matcher.group(5));
                        final List<Range<Integer>> rangeList =
                                Objects.requireNonNull(current).getRanges().getOrDefault(day, new ArrayList<>());
                        current.getRanges().put(day, rangeList);
                    }
                    case "wakes" -> {
                        wakes = Integer.parseInt(matcher.group(5));
                        Objects.requireNonNull(current).getRanges().get(day).add(Range.of(falls, wakes));
                    }
                    default -> throw new RuntimeException(rec);
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
        @NonNull    // day      falls -> wakes
        private Map<Integer, List<Range<Integer>>> ranges = new LinkedHashMap<>();

        public Guard(final int newId) {
            id = newId;
        }

        public int getTimeAsleep() {
            return getRanges().values().stream()
                    .flatMap(Collection::stream)
                    .mapToInt(range -> (range.getMaximum() - range.getMinimum()))
                    .sum();
        }

        public int[] getSleepTallies() {
            final List<Range<Integer>> rangeList = getRanges().values().stream()
                    .flatMap(Collection::stream).toList();

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
