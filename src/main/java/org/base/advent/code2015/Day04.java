package org.base.advent.code2015;

import java.io.IOException;
import java.security.MessageDigest;
import org.apache.commons.lang3.StringUtils;
import org.base.advent.Solution;


/**
 * <h2>Part 1</h2>
 * Santa needs help mining some AdventCoins (very similar to bitcoins) to use as gifts for all the economically
 * forward-thinking little girls and boys.
 *
 * To do this, he needs to find MD5 hashes which, in hexadecimal, start with at least five zeroes. The input to
 * the MD5 hash is some secret key (your puzzle input, given below) followed by a number in decimal. To mine
 * AdventCoins, you must find Santa the lowest positive number (no leading zeroes: 1, 2, 3, ...) that produces
 * such a hash.
 *
 * For example:
 *
 * If your secret key is abcdef, the answer is 609043, because the MD5 hash of abcdef609043 starts with five
 * zeroes (000001dbbfa...), and it is the lowest such number to do so.
 *
 * If your secret key is pqrstuv, the lowest number it combines with to make an MD5 hash starting with five
 * zeroes is 1048970; that is, the MD5 hash of pqrstuv1048970 looks like 000006136ef....
 * 
 * <h2>Part 2</h2>
 * Now find one that starts with six zeroes.
 */
public class Day04 implements Solution<String>{

    @Override
    public String getInput() throws IOException {
        return "bgvyzdsv";
    }

    @Override
    public Object solvePart1() throws Exception {
        return solveFor("00000");
    }

    @Override
    public Object solvePart2() throws Exception {
        return solveFor("000000");
    }

    protected long solveFor(final String prefix) throws Exception {
		final String input = getInput();
        long index = 1L;
        for (; index < Long.MAX_VALUE; index += 1L) {
            final String result = convertToMD5ThenHex(input + String.valueOf(index));
            if (StringUtils.startsWith(result, prefix)) {
                return index;
            }
        }
        
        return -1;
    }

    protected String convertToMD5ThenHex(final String text) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
//            String encrypted = new String(digest.digest(text.getBytes()));
            return bytesToHex(digest.digest(text.getBytes()));
        }
        catch (final Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    protected static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(final byte[] bytes) {
        final char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            final int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
