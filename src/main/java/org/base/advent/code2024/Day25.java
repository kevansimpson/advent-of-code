package org.base.advent.code2024;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.base.advent.util.Util.splitByBlankLine;

/**
 * <a href="https://adventofcode.com/2024/day/25">Day 25</a>
 */
public class Day25 implements Function<List<String>, Pair<Integer, Integer>> {
    @Override
    public Pair<Integer, Integer> apply(List<String> input) {
        List<List<String>> data = splitByBlankLine(input);
        List<int[]> keys = new ArrayList<>();
        List<int[]> locks = new ArrayList<>();
        for (List<String> schematic : data) {
            if (schematic.get(0).equals("#####")) // lock
                locks.add(toHeights(schematic));
            else // key
                keys.add(toHeights(schematic));
        }

        int fits = 0;
        for (int[] k : keys) {
            for (int[] l : locks) {
                if (fits(k, l))
                    fits++;
            }
        }
        return Pair.of(fits, 0);
    }

    boolean fits(int[] key, int[] lock) {
        for (int i = 0; i < 5; i++)
            if (key[i] + lock[i] > 5)
                return false;
        return true;
    }

    int[] toHeights(List<String> keyOrLock) {
        int[] heights = new int[5];
        Arrays.fill(heights, -1);
        for (String kl : keyOrLock)
            for (int i = 0; i < 5; i++)
                if (kl.charAt(i) == '#')
                    heights[i]++;
        return heights;
    }
}

