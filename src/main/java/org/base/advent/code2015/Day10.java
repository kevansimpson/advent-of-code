package org.base.advent.code2015;


import org.base.advent.Solution;

import java.io.IOException;

/**
 * <h2>Part 1</h2>
 * Today, the Elves are playing a game called look-and-say. They take turns making sequences by reading aloud the
 * previous sequence and using that reading as the next sequence. For example, 211 is read as "one two, two ones",
 * which becomes 1221 (1 2, 2 1s).
 *
 * Look-and-say sequences are generated iteratively, using the previous value as input for the next step. For each
 * step, take the previous value, and replace each run of digits (like 111) with the number of digits (3) followed
 * by the digit itself (1).
 *
 * For example:
 *  - 1 becomes 11 (1 copy of digit 1).
 *  - 11 becomes 21 (2 copies of digit 1).
 *  - 21 becomes 1211 (one 2 followed by one 1).
 *  - 1211 becomes 111221 (one 1, one 2, and two 1s).
 *  - 111221 becomes 312211 (three 1s, two 2s, and one 1).
 *
 * Starting with the digits in your puzzle input, apply this process 40 times. What is the length of the result?
 *
 * <h2>Part 2</h2>
 * Neat, right? You might also enjoy hearing John Conway talking about this sequence (that's Conway of Conway's
 * Game of Life fame).
 *
 * Now, starting again with the digits in your puzzle input, apply this process 50 times.
 * What is the length of the new result?
 *
 */
public class Day10 implements Solution<String> {
    @Override
    public String getInput() throws IOException {
        return "1321131112";
    }

    @Override
    public Object solvePart1() throws Exception {
        return lookAndSay(getInput(), 40).length();
    }

    @Override
    public Object solvePart2() throws Exception {
        return lookAndSay(getInput(), 50).length();
    }

    public String lookAndSay(String input, final int count) {
        for (int i = 0; i < count; i++) {
            input = lookAndSay(input);
        }
        return input;
    }
    
    public String lookAndSay(final String input) {
        int count = 0;
        char digit = input.charAt(0);
        final StringBuilder bldr = new StringBuilder();

        for (final char ch : input.toCharArray()) {
            if (digit == ch) {
                count +=1;
            }
            else {
                bldr.append(count).append(digit);
                digit = ch;
                count = 1;
            }
        }

        bldr.append(count).append(digit);
        return bldr.toString();
    }
}
