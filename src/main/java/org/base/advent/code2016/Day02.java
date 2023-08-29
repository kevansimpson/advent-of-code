package org.base.advent.code2016;

import org.base.advent.Solution;
import org.base.advent.util.Point;

import java.util.List;
import java.util.Map;


/**
 * <a href="https://adventofcode.com/2016/day/02">Day 02</a>
 */
public class Day02 implements Solution<List<String>> {
    @Override
    public Object solvePart1(final List<String> input) {
        return followInstructions(input, squarePad);
    }

    @Override
    public Object solvePart2(final List<String> input) {
        return followInstructions(input, diamondPad);
    }

    String followInstructions(final List<String> list, final Map<Point, Character> buttonPad) {
        final StringBuilder builder = new StringBuilder(list.size());
        Point point = buttonPad.entrySet().stream()
                .filter(entry -> entry.getValue() == '5')
                .map(Map.Entry::getKey)
                .findFirst().orElse(Point.ORIGIN);

        for (final String instruction : list) {
            Point start = point;
            for (char dir : instruction.toCharArray()) {
                Point next = start.move(String.valueOf(dir));

                if (buttonPad.containsKey(next))
                    start = next;
            }

            builder.append(buttonPad.get(start));
            point = start;
        }

        return builder.toString();
    }

    static final Map<Point, Character> squarePad = Map.of(
        new Point(-1, 1), '1',
        new Point(0, 1), '2',
        new Point(1, 1), '3',
        new Point(-1, 0), '4',
        Point.ORIGIN, '5',
        new Point(1, 0), '6',
        new Point(-1, -1), '7',
        new Point(0, -1), '8',
        new Point(1, -1), '9');

    static final Map<Point, Character> diamondPad = Map.ofEntries( // more than 10, can't use Map.of
            Map.entry(new Point(0, 2), '1'),
            Map.entry(new Point(-1, 1), '2'),
            Map.entry(new Point(0, 1), '3'),
            Map.entry(new Point(1, 1), '4'),
            Map.entry(new Point(-2, 0), '5'),
            Map.entry(new Point(-1, 0), '6'),
            Map.entry(Point.ORIGIN, '7'),
            Map.entry(new Point(1, 0), '8'),
            Map.entry(new Point(2, 0), '9'),
            Map.entry(new Point(-1, -1), 'A'),
            Map.entry(new Point(0, -1), 'B'),
            Map.entry(new Point(1, -1), 'C'),
            Map.entry(new Point(0, -2), 'D'));
}
