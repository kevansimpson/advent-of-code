package org.base.advent.code2015;

import org.base.advent.Solution;

import java.util.regex.Pattern;

/**
 * <a href="https://adventofcode.com/2015/day/11">Day 11</a>
 */
public class Day11 implements Solution<String> {

    private static final Pattern disallowed = Pattern.compile("[^iol]+", Pattern.CASE_INSENSITIVE);

    @Override
    public String getInput(){
        return "vzbxkghb";
    }

    @Override
    public Object solvePart1() {
        return nextValidPassword(getInput());
    }

    @Override
    public Object solvePart2() {
        return nextValidPassword("vzbxxyzz");   // we know answer from first part... save some time
    }

    public String nextValidPassword(final String input) {
        String pswd = nextPassword(input);
        while (!isValidPassword(pswd)) {
            pswd = nextPassword(pswd);
        }
        
        return pswd;
    }

    protected String nextPassword(final String input) {
        final char[] ltrs = input.toCharArray();

        for (int i = ltrs.length - 1; i >= 0; i--) {
            final char nextLtr = nextLetter(ltrs[i]);
            ltrs[i] = nextLtr;
            if (nextLtr != 'a')
                break;
        }

        return new String(ltrs);
    }

    protected char nextLetter(final char ltr) {
        final char next = (ltr == 'z') ? 'a' : (char) (1 + ((int) ltr));

        switch (next) {
            case 'i':
                return 'j';
            case 'l':
                return 'm';
            case 'o':
                return 'p';
            default:
                return next;
        }
    }

    protected boolean isValidPassword(final String pswd) {
        return !isDisallowed(pswd) && hasNonOverlappingPairs(pswd) && hasSequence(pswd);
    }

    protected boolean hasSequence(final String pswd) {
        int count = 1;
        int current = 0;
        final char[] letters = pswd.toCharArray();
        for (final char ch : letters) {
            if (((int) ch) == (current + 1)) {
                count += 1;
                if (count >= 3)
                    return true;
            }
            else {
                count = 1;
            }
            current = (int) ch;
        }

        return false;
    }

    protected boolean isDisallowed(final String pswd) {
        return !disallowed.matcher(pswd).matches();
    }

    protected boolean hasNonOverlappingPairs(final String pswd) {
        return ("Q"+pswd+"Q").split("([a-z])\\1").length >= 3;
    }
}
