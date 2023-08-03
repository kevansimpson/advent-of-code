package org.base.advent.code2016;

import org.base.advent.Solution;
import org.base.advent.util.Point;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <a href="https://adventofcode.com/2016/day/02">Day 02</a>
 */
public class Day02 implements Solution<List<String>> {

    @Override
    public List<String> getInput(){
        return readLines("/2016/input02.txt");
    }

    @Override
    public Object solvePart1() {
        return followInstructions(getInput(), squarePad());
    }

    @Override
    public Object solvePart2() {
        return followInstructions(getInput(), diamondPad());
    }

    public String followInstructions(final List<String> list, final Map<Point, Character> buttonPad) {
        final StringBuilder builder = new StringBuilder(list.size());
        Point point = buttonPad.entrySet().stream()
                .filter(entry -> entry.getValue() == '5')
                .map(Map.Entry::getKey)
                .findFirst().orElse(Point.ORIGIN);

        for (final String instruction : list) {
            Point start = point;
            for (char dir : instruction.toCharArray()) {
                Point next = null;
                switch (dir) {
                    case 'U':
                        next = start.up(1);
                        break;
                    case 'D':
                        next = start.down(1);
                        break;
                    case 'L':
                        next = start.left(1);
                        break;
                    case 'R':
                        next = start.right(1);
                        break;
                }

                if (buttonPad.containsKey(next))
                    start = next;
            }

            builder.append(buttonPad.get(start));
            point = start;
        }

        return builder.toString();
    }

    Map<Point, Character> squarePad() {
        final Map<Point, Character> pad = new HashMap<>();
        pad.put(new Point(-1, 1), '1');
        pad.put(new Point(0, 1), '2');
        pad.put(new Point(1, 1), '3');
        pad.put(new Point(-1, 0), '4');
        pad.put(Point.ORIGIN, '5');
        pad.put(new Point(1, 0), '6');
        pad.put(new Point(-1, -1), '7');
        pad.put(new Point(0, -1), '8');
        pad.put(new Point(1, -1), '9');

        return pad;
    }

    Map<Point, Character> diamondPad() {
        final Map<Point, Character> pad = new HashMap<>();
        pad.put(new Point(0, 2), '1');
        pad.put(new Point(-1, 1), '2');
        pad.put(new Point(0, 1), '3');
        pad.put(new Point(1, 1), '4');
        pad.put(new Point(-2, 0), '5');
        pad.put(new Point(-1, 0), '6');
        pad.put(Point.ORIGIN, '7');
        pad.put(new Point(1, 0), '8');
        pad.put(new Point(2, 0), '9');
        pad.put(new Point(-1, -1), 'A');
        pad.put(new Point(0, -1), 'B');
        pad.put(new Point(1, -1), 'C');
        pad.put(new Point(0, -2), 'D');

        return pad;
    }
}
