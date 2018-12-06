package org.base.advent.code2016;

import lombok.Getter;
import lombok.ToString;
import org.base.advent.Solution;
import org.base.advent.util.Point;

import java.io.IOException;
import java.util.*;


/**
 * <h2>Part 1</h2>
 * You arrive at Easter Bunny Headquarters under cover of darkness. However, you left in such a rush
 * that you forgot to use the bathroom! Fancy office buildings like this one usually have keypad locks
 * on their bathrooms, so you search the front desk for the code.
 *
 * "In order to improve security," the document you find says, "bathroom codes will no longer be written
 * down. Instead, please memorize and follow the procedure below to access the bathrooms."
 *
 * The document goes on to explain that each button to be pressed can be found by starting on the previous
 * button and moving to adjacent buttons on the keypad: U moves up, D moves down, L moves left, and
 * R moves right. Each line of instructions corresponds to one button, starting at the previous button
 * (or, for the first line, the "5" button); press whatever button you're on at the end of each line.
 * If a move doesn't lead to a button, ignore it.
 *
 * You can't hold it much longer, so you decide to figure out the code as you walk to the bathroom.
 * You picture a keypad like this:
 * <pre>
 * 1 2 3
 * 4 5 6
 * 7 8 9
 * </pre>
 *
 * Suppose your instructions are:
 * <pre>
 *     ULL
 *     RRDDD
 *     LURDL
 *     UUUUD
 * </pre>
 *
 * You start at "5" and move up (to "2"), left (to "1"), and left (you can't, and stay on "1"),
 * so the first button is 1.
 *
 * Starting from the previous button ("1"), you move right twice (to "3") and then down three times
 * (stopping at "9" after two moves and ignoring the third), ending up with 9.
 *
 * Continuing from "9", you move left, up, right, down, and left, ending with 8.
 *
 * Finally, you move up four times (stopping at "2"), then down once, ending with 5.
 * So, in this example, the bathroom code is 1985.
 *
 * Your puzzle input is the instructions from the document you found at the front desk.
 * What is the bathroom code?
 *
 * <h2>Part 2</h2>
 */
public class Day02 implements Solution<List<String>> {

    @Override
    public List<String> getInput() throws IOException {
        return readLines("/2016/input02.txt");
    }

    @Override
    public Object solvePart1() throws Exception {
        return followInstructions(getInput(), squarePad());
    }

    @Override
    public Object solvePart2() throws Exception {
        return followInstructions(getInput(), diamondPad());
    }

    public String followInstructions(final List<String> list, final Map<Point, Character> buttonPad) {
        final StringBuilder builder = new StringBuilder(list.size());
        Point point = buttonPad.entrySet().stream()
                .filter(entry -> entry.getValue() == '5')
                .map(Map.Entry::getKey)
                .findFirst().orElse(null);

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
