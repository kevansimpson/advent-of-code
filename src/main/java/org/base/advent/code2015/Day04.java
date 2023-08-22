package org.base.advent.code2015;

import org.apache.commons.lang3.StringUtils;
import org.base.advent.Solution;

import java.security.MessageDigest;

/**
 * <a href="https://adventofcode.com/2015/day/04">Day 04</a>
 */
public class Day04 implements Solution<String>{
    @Override
    public Object solvePart1(final String input) {
        return solveFor(input, "00000");
    }

    @Override
    public Object solvePart2(final String input) {
        return solveFor(input, "000000");
    }

    long solveFor(final String input, final String prefix) {
        long index = 1L;
        for (; index < Long.MAX_VALUE; index += 1L) {
            final String result = convertToMD5ThenHex(input + index);
            if (StringUtils.startsWith(result, prefix)) {
                return index;
            }
        }
        
        return -1;
    }

    String convertToMD5ThenHex(final String text) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            return bytesToHex(digest.digest(text.getBytes()));
        }
        catch (final Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
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
