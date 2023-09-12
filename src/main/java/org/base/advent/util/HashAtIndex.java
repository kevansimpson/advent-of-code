package org.base.advent.util;

import org.apache.commons.lang3.ArrayUtils;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static org.base.advent.util.HashCache.newMD5;

public record HashAtIndex(String input, String hash, long index) {
    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    /**
     * Converts the given hex character to its base 10 numeric value.
     * @param ch A single character.
     * @return character's base 10 numeric value or -1.
     */
    public static int hexCharToInt(char ch) {
        return ArrayUtils.indexOf(HEX_ARRAY, ch);
    }

    /**
     * Returns list of {@link HashAtIndex} of length <code>count</code>
     * matching the given predicate.
     *
     * @param hashAtIndex Contains md5 input and starting index, hash is ignored.
     * @param predicate The hash predicate to determine whether to include in results.
     * @param count The expected number of matches. Behavior is undetermined if:
     *              end index goes beyond boundaries or {@link Long#MAX_VALUE}.
     * @return list of matching hashes with their respective indexes.
     */
    public static List<HashAtIndex> nextWithList(HashAtIndex hashAtIndex,
                                                 Predicate<String> predicate,
                                                 long count) {
        return nextWithList(hashAtIndex, predicate, count, Long.MAX_VALUE);
    }

    /**
     * Returns list of {@link HashAtIndex} of length <code>count</code>
     * matching the given predicate.
     *
     * @param hashAtIndex Contains md5 input and starting index, hash is ignored.
     * @param predicate The hash predicate to determine whether to include in results.
     * @param count The expected number of matches. Behavior is undetermined if:
     *              end index goes beyond <code>endIndexExclusive</code>.
     * @param endIndexExclusive The upper range to search for matching hashes.
     * @return list of matching hashes with their respective indexes.
     */
    public static List<HashAtIndex> nextWithList(HashAtIndex hashAtIndex,
                                                 Predicate<String> predicate,
                                                 long count,
                                                 long endIndexExclusive) {
        final List<HashAtIndex> list = new ArrayList<>();
        final MessageDigest digest = newMD5();
        HashAtIndex next = hashAtIndex;
        for (long i = 0; i < count; i++) {
            next = nextWith(next, digest, predicate, endIndexExclusive);
            digest.reset();
            if (next != null)
                list.add(next);
            else
                break;
        }

        return list;
    }

    /**
     * Returns next {@link HashAtIndex} matching the given predicate or <code>null</code>.
     *
     * @param hashAtIndex Contains md5 input and starting index, hash is ignored.
     * @param predicate The hash predicate to determine whether to include in results.
     * @return the next match or <code>null</code>.
     */
    public static HashAtIndex nextWith(HashAtIndex hashAtIndex, Predicate<String> predicate) {
        return nextWith(hashAtIndex, predicate, Long.MAX_VALUE);
    }

    /**
     * Returns next {@link HashAtIndex} matching the given predicate or <code>null</code>.
     *
     * @param hashAtIndex Contains md5 input and starting index, hash is ignored.
     * @param predicate The hash predicate to determine whether to include in results.
     * @param endIndexExclusive The upper range to search for matching hashes.
     * @return the next match or <code>null</code>.
     */
    public static HashAtIndex nextWith(HashAtIndex hashAtIndex,
                                       Predicate<String> predicate,
                                       long endIndexExclusive) {
        final List<HashAtIndex> list = nextWithList(hashAtIndex, predicate, 1L, endIndexExclusive);
        return list.isEmpty() ? null : list.get(0);
    }


    /**
     * Returns next {@link HashAtIndex} matching the given predicate or <code>null</code>.
     *
     * @param hashAtIndex Contains md5 input and starting index, hash is ignored.
     * @param digest A new or reset {@link MessageDigest}.
     * @param predicate The hash predicate to determine whether to include in results.
     * @param endIndexExclusive The upper range to search for matching hashes.
     * @return the next match or <code>null</code>.
     */
    static HashAtIndex nextWith(HashAtIndex hashAtIndex,
                                MessageDigest digest,
                                Predicate<String> predicate,
                                long endIndexExclusive) {
        for (long i = hashAtIndex.index + 1L; i < endIndexExclusive; i += 1L) {
            final String result = convertToMD5ThenHex(digest, hashAtIndex.input + i);
            if (predicate.test(result)) {
                return new HashAtIndex(hashAtIndex.input, result, i);
            }
        }

        return null;
    }

    static String bytesToHex(final byte[] bytes) {
        final char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            final int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Hashes given text and converts to hexidecimal.
     *
     * @param digest A new or reset {@link MessageDigest}.
     * @param text The text to convert.
     * @return hexidecimal hash of given text.
     */
    public static String convertToMD5ThenHex(MessageDigest digest, final String text) {
        return bytesToHex(digest.digest(text.getBytes()));
    }
}
