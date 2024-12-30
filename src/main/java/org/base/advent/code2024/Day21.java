package org.base.advent.code2024;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.util.Point;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;

/**
 * <a href='https://adventofcode.com/2024/day/21'>Day 21</a>
 */
public class Day21 implements Function<List<String>, Pair<Long, Long>> {
    private static final char[][] NUMERIC_KEYPAD = new char[][] {
            {'7','8','9'}, {'4','5','6'},{'1','2','3'}, {' ','0','A'},
    };

    private static final char[][] DIRECTIONAL_KEYPAD = new char[][] {
            {' ','^','A'}, {'<','v','>'},
    };

    @Override
    public Pair<Long, Long> apply(List<String> input) {
        Map<Move, String> numberPad = toMoveMap(toPointMap(NUMERIC_KEYPAD), true);
        Map<Move, String> directionalPad = toMoveMap(toPointMap(DIRECTIONAL_KEYPAD), false);
        Map<PathKey, Long> cache = new HashMap<>();
        long part1 = 0L, part2 = 0L;
        for (String code : input) {
            long num = Long.parseLong(code.replaceAll("A", ""));
            String robot1 = firstRobot(code, numberPad);
            part1 += num * count(robot1, directionalPad, cache, 2);;
            part2 += num * count(robot1, directionalPad, cache, 25);
        }

        return Pair.of(part1, part2);
    }

    record Move(char start, char end) {}
    record PathKey(String path, int depth) {}

    String firstRobot(String code, Map<Move, String> numberPad) {
        StringBuilder moves = new StringBuilder();
        moves.append(numberPad.get(new Move('A', code.charAt(0)))).append('A');
        for (int i = 1; i < code.length(); i++)
            moves.append(numberPad.get(new Move(code.charAt(i - 1), code.charAt(i)))).append('A');
        return moves.toString();
    }

    long count(String code, Map<Move, String> directionalPad, Map<PathKey, Long> cache, int depth) {
        if (depth == 0)
            return code.length();
        if ("A".equals(code))
            return 1L;

        PathKey key = new PathKey(code, depth);
        if (cache.containsKey(key))
            return cache.get(key);

        long count = 0L;
        for (String move : code.split("A")) {
            StringBuilder moves = new StringBuilder();
            move = "A" + move + "A";
            for (int i = 1; i < move.length(); i++) {
                Move m = new Move(move.charAt(i - 1), move.charAt(i));
                moves.append(directionalPad.get(m)).append('A');
            }
            count += count(moves.toString(), directionalPad, cache, depth - 1);
        }
        cache.put(key, count);
        return count;
    }

    Map<Move, String> toMoveMap(Map<Character, Point> pts, boolean isNumberPad) {
        Map<Move, String> moves = new HashMap<>();
        for (Entry<Character, Point> start : pts.entrySet())
            for (Entry<Character, Point> end : pts.entrySet()) {
                moves.put(new Move(start.getKey(), end.getKey()), generateMove(start, end, isNumberPad));
            }
        return moves;
    }

    String generateMove(Entry<Character, Point> start, Entry<Character, Point> end, boolean isNumberPad) {
        Point s = start.getValue(), e = end.getValue();
        int dx = e.ix() - s.ix(), dy = e.iy() - s.iy();
        String h = horizontal(dx), v = vertical(dy);
        String path1 = h + v, path2 = v + h;

        if (isNumberPad) {
            if (s.iy() == 3 && e.ix() == 0)
                return path2;
            if (s.ix() == 0 && e.iy() == 3)
                return path1;
        }
        else {
            if (s.ix() == 0)
                return path1;
            if (e.ix() == 0)
                return path2;
        }

        return (dx > 0) ? path2 : path1;
    }

    String horizontal(int dx) {
        return (dx == 0) ? "" : StringUtils.leftPad("", Math.abs(dx), (dx < 0) ? "<" : ">");
    }

    String vertical(int dy) {
        return (dy == 0) ? "" : StringUtils.leftPad("", Math.abs(dy), (dy < 0) ? "^" : "v");
    }

    Map<Character, Point> toPointMap(char[][] keypad) {
        Map<Character, Point> pts = new HashMap<>();
        for (int y = 0; y < keypad.length; y++)
            for (int x = 0; x < keypad[0].length; x++) {
                char ch = keypad[y][x];
                if (ch != ' ')
                    pts.put(ch, Point.of(x, y));
            }
        return pts;
    }
}

