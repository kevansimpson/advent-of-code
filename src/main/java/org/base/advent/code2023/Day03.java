package org.base.advent.code2023;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.util.Point;
import org.base.advent.util.Text;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * <a href="https://adventofcode.com/2023/day/3">Day 3</a>
 */
public class Day03 implements Function<List<String>, Pair<Integer, Integer>> {

    private static final Pattern NUMS = Pattern.compile("[0-9]+");

    @Override
    public Pair<Integer, Integer> apply(List<String> input) {
        Schematic schematic = new Schematic(input);
        int sum = 0, product = 0;
        for (Point symbol : schematic.symbols.keySet()) {
            List<Point> around = symbol.surrounding();
            List<Integer> gears = new ArrayList<>();
            for (Pair<Integer, Set<Point>> parts : schematic.partNumbers) {
                Integer num = parts.getLeft();
                Set<Point> pos = parts.getRight();
                if (!CollectionUtils.intersection(around, pos).isEmpty()) {
                    sum += num;
                    if ('*' == schematic.symbols.get(symbol))
                        gears.add(num);
                }
            }
            if (gears.size() == 2)
                product += (gears.get(0) * gears.get(1));
        }

        return Pair.of(sum, product);
    }

    private static class Schematic {
        final int size;
        final Map<Point, Character> symbols = new HashMap<>();
        final Set<Pair<Integer, Set<Point>>> partNumbers = new HashSet<>();

        public Schematic(List<String> input) {
            size = input.size();
            for (int r = size - 1; r >=0; r--) {
                String line = input.get(r);
                List<String> nums = Text.findAll(NUMS, line);
                int index = 0, offset = 0;
                for (String num : nums) {
                    Set<Point> parts = new HashSet<>();
                    if (index >= line.length())
                        break;
                    int next = line.substring(index).indexOf(num);
                    for (int i = next + offset; i < next + offset + num.length(); i++)
                        parts.add(new Point(size - r - 1, i));
                    partNumbers.add(Pair.of(Integer.parseInt(num), parts));
                    index += (next + num.length());
                    offset += (next + num.length());
                }

                for (int c = 0; c < size; c++) {
                    char sym = line.charAt(c);
                    if (String.valueOf(sym).matches("([^.0-9])"))
                        symbols.put(new Point(size - r - 1, c), sym);
                }
            }
        }
    }
}
