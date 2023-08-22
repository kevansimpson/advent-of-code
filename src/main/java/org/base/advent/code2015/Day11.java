package org.base.advent.code2015;

import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * <a href="https://adventofcode.com/2015/day/11">Day 11</a>
 */
public class Day11 implements Function<String, Day11.NextTwoPasswords> {
    private static final Pattern disallowed = Pattern.compile("[^iol]+", Pattern.CASE_INSENSITIVE);

    public record NextTwoPasswords(String one, String two) {}

    @Override
    public NextTwoPasswords apply(String input) {
        String one = nextValidPassword(input);
        return new NextTwoPasswords(one, nextValidPassword(one));
    }

    String nextValidPassword(final String input) {
        String pswd = nextPassword(input);
        while (!isValidPassword(pswd)) {
            pswd = nextPassword(pswd);
        }
        
        return pswd;
    }

    String nextPassword(final String input) {
        final char[] ltrs = input.toCharArray();

        for (int i = ltrs.length - 1; i >= 0; i--) {
            final char nextLtr = nextLetter(ltrs[i]);
            ltrs[i] = nextLtr;
            if (nextLtr != 'a')
                break;
        }

        return new String(ltrs);
    }

    char nextLetter(final char ltr) {
        final char next = (ltr == 'z') ? 'a' : (char) (1 + ((int) ltr));
        return switch (next) {
            case 'i' -> 'j';
            case 'l' -> 'm';
            case 'o' -> 'p';
            default -> next;
        };
    }

    boolean isValidPassword(final String pswd) {
        return !isDisallowed(pswd) && hasNonOverlappingPairs(pswd) && hasSequence(pswd);
    }

    boolean hasSequence(final String pswd) {
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
            current = ch;
        }

        return false;
    }

    boolean isDisallowed(final String pswd) {
        return !disallowed.matcher(pswd).matches();
    }

    boolean hasNonOverlappingPairs(final String pswd) {
        return ("Q"+pswd+"Q").split("([a-z])\\1").length >= 3;
    }
}
