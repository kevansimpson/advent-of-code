package org.base.advent.code2024;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * <a href="https://adventofcode.com/2024/day/11">Day 11</a>
 *
 */
public class Day11 implements Function<String, Pair<Long, Long>> {
    @Override
    public Pair<Long, Long> apply(String input) {
        List<Long> nums = Stream.of(input.split(" ")).mapToLong(Long::parseLong).boxed().toList();
        Map<Long, Long> counter = new HashMap<>();
        for (Long mark : nums)
            counter.put(mark, counter.getOrDefault(mark, 0L) + 1L);

        for (int i = 0; i < 25; i++)
            counter = blink(counter);
        long count1 = 0L;
        for (Long val : counter.values())
            count1 += val;

        for (int i = 0; i < 50; i++)
            counter = blink(counter);
        long count2 = 0L;
        for (Long val : counter.values())
            count2 += val;

        return Pair.of(count1, count2);
    }

    private Map<Long, Long> blink(Map<Long, Long> stones) {
        Map<Long, Long> counter = new HashMap<>();
        for (Map.Entry<Long, Long> e : stones.entrySet()) {
            if (e.getKey() == 0)
                counter.put(1L, counter.getOrDefault(1L, 0L) + e.getValue());
            else {
                String val = String.valueOf(e.getKey());
                if ((val.length() % 2) == 0) {
                    Long left = Long.parseLong(val.substring(0, val.length() / 2));
                    Long right = Long.parseLong(val.substring(val.length() / 2));
                    counter.put(left, counter.getOrDefault(left, 0L) + e.getValue());
                    counter.put(right, counter.getOrDefault(right, 0L) + e.getValue());
                }
                else {
                    Long mult = 2024L * e.getKey();
                    counter.put(mult, counter.getOrDefault(mult, 0L) + e.getValue());
                }
            }
        }
        return counter;
    }
}

