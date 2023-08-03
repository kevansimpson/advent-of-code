package org.base.advent.code2015;


import org.base.advent.Solution;

/**
 * <a href="https://adventofcode.com/2015/day/10">Day 10</a>
 */
public class Day10 implements Solution<String> {
    @Override
    public String getInput(){
        return "1321131112";
    }

    @Override
    public Object solvePart1() {
        return lookAndSay(getInput(), 40).length();
    }

    @Override
    public Object solvePart2() {
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
