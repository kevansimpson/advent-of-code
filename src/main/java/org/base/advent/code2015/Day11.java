package org.base.advent.code2015;

import org.base.advent.Solution;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * <h2>Part 1</h2>
 * Santa's previous password expired, and he needs help choosing a new one.
 *
 * To help him remember his new password after the old one expires, Santa has devised a method of coming up with a
 * password based on the previous one. Corporate policy dictates that passwords must be exactly eight lowercase letters
 * (for security reasons), so he finds his new password by incrementing his old password string repeatedly until it is valid.
 *
 * Incrementing is just like counting with numbers: xx, xy, xz, ya, yb, and so on. Increase the rightmost letter one
 * step; if it was z, it wraps around to a, and repeat with the next letter to the left until one doesn't wrap around.
 *
 * Unfortunately for Santa, a new Security-Elf recently started, and he has imposed some additional password requirements:
 *
 *  - Passwords must include one increasing straight of at least three letters, like abc, bcd, cde, and so on, up to xyz.
 *      They cannot skip letters; abd doesn't count.
 *  - Passwords may not contain the letters i, o, or l, as these letters can be mistaken for other characters and are
 *      therefore confusing.
 *  - Passwords must contain at least two different, non-overlapping pairs of letters, like aa, bb, or zz.
 *
 * For example:
 *  - hijklmmn meets the first requirement (because it contains the straight hij) but fails the second requirement
 *      requirement (because it contains i and l).
 *  - abbceffg meets the third requirement (because it repeats bb and ff) but fails the first requirement.
 *  - abbcegjk fails the third requirement, because it only has one double letter (bb).
 *  - The next password after abcdefgh is abcdffaa.
 *  - The next password after ghijklmn is ghjaabcc, because you eventually skip all the passwords that start with
 *      ghi..., since i is not allowed.
 *
 * Given Santa's current password (your puzzle input), what should his next password be?
 *
 * <h2>Part 2</h2>
 * Santa's password expired again. What's the next one?
 *
 */
public class Day11 implements Solution<String> {

    private static Pattern disallowed = Pattern.compile("[^iol]+", Pattern.CASE_INSENSITIVE);

    @Override
    public String getInput() throws IOException {
        return "vzbxkghb";
    }

    @Override
    public Object solvePart1() throws Exception {
        return nextValidPassword(getInput());
    }

    @Override
    public Object solvePart2() throws Exception {
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
