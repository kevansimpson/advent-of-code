package org.base.advent.code2024;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.Function;

/**
 * <a href="https://adventofcode.com/2024/day/22">Day 22</a>
 */
public class Day22 implements Function<List<Integer>, Pair<Long, Integer>> {
    @Override
    public Pair<Long, Integer> apply(List<Integer> input) {
        Map<Integer, Integer> bananaMap = new HashMap<>();
        long part1 = 0L;
        for (int secret : input) {
            part1 += iterate(secret, bananaMap);
        }

        return Pair.of(part1, bananaMap.values().stream().max(Comparator.naturalOrder()).orElseThrow());
    }

    long iterate(long secret, Map<Integer, Integer> bananaMap) {
        long[] nums = new long[2000];
        int[] deltas = new int[2000];
        LinkedList<Integer> test = new LinkedList<>();
        Set<Integer> allSequences = new HashSet<>();
        int price = (int) secret % 10;

        for (int i = 0; i < 2000; i++) {
            secret = process(secret);
            nums[i] = secret;
            int p = (int) secret % 10;
            deltas[i] = p - price;
            test.add(deltas[i]);
            price = p;

            if (i > 3) {
                test.removeFirst();
                int key = arrayKey(test);
                if (!allSequences.contains(key)) {
                    allSequences.add(key);
                    bananaMap.put(key, bananaMap.getOrDefault(key, 0) + price);
                }
            }
        }
        return nums[1999];
    }

    long process(long secret) {
        long prune1 = (secret ^ secret * 64L) % 16777216L;
        long prune2 = (prune1 ^ Math.floorDiv(prune1, 32L)) % 16777216L;
        return (prune2 ^ prune2 * 2048L) % 16777216L;
    }

    int arrayKey(List<Integer> list) {
        int key = (list.get(0) + 9) * 100000000;
        key += (list.get(1) + 9) * 1000000;
        key += (list.get(2) + 9) * 10000;
        key += (list.get(3) + 9) * 100;
        return key;
    }
}
