package org.base.advent.code2016;

import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.util.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.countMatches;

/**
 * <a href="https://adventofcode.com/2016/day/6">Day 6</a>
 */
public class Day06 implements Function<List<String>, Pair<String, String>> {
    @Override
    public Pair<String, String> apply(List<String> input) {
        List<String> columns = Text.columns(input);
        List<Map<Character, Integer>> columnCounts = columnCounts(columns);
        char[] correctedVersion = new char[8], originalMessage = new char[8];

        for (int i = 0; i < columnCounts.size(); i++) {
            Map<Character, Integer> cmap = columnCounts.get(i);
            int min = 50, max = 0;
            for (Map.Entry<Character, Integer> kv : cmap.entrySet()) {
                if (kv.getValue() < min) {
                    originalMessage[i] = kv.getKey();
                    min = kv.getValue();
                }

                if (kv.getValue() > max) {
                    correctedVersion[i] = kv.getKey();
                    max = kv.getValue();
                }
            }
        }

        return Pair.of(new String(correctedVersion), new String(originalMessage));
    }

    List<Map<Character, Integer>> columnCounts(List<String> columns) {
        List<Map<Character, Integer>> counts = new ArrayList<>();
        for (String col : columns) {
            Map<Character, Integer> cmap = new HashMap<>();
            for (char ch : col.toCharArray())
                cmap.put(ch, countMatches(col, ch));

            counts.add(cmap);
        }

        return counts;
    }
}
