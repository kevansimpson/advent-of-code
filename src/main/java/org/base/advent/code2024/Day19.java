package org.base.advent.code2024;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.Function;

/**
 * <a href="https://adventofcode.com/2024/day/19">Day 19</a>
 */
public class Day19 implements Function<List<String>, Pair<Integer, Long>> {
    @Override
    public Pair<Integer, Long> apply(List<String> input) {
        Set<String> patterns = new HashSet<>(Arrays.asList(input.get(0).split(", ")));
        List<String> desired = input.subList(2, input.size());
        int possible = 0;
        long count = 0L;
        Map<String, Long> cache = new HashMap<>();
        for (String d : desired) {
            long c = countPossibleDesigns(d, patterns, cache);
            if (c > 0)
                possible++;
            count += c;
        }

        return Pair.of(possible, count);
    }

    long countPossibleDesigns(String design, Set<String> patterns, Map<String, Long> cache) {
        if (design.isEmpty())
            return 1L;
        if (cache.containsKey(design))
            return cache.get(design);

        long count = 0L;
        for (String p : patterns) {
            if (design.startsWith(p))
                count += countPossibleDesigns(design.substring(p.length()), patterns, cache);
        }

        cache.put(design, count);
        return count;
    }
}

