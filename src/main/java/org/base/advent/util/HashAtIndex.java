package org.base.advent.util;

import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public record HashAtIndex(String input, String hash, long index) {
    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    public List<HashAtIndex> nextWith(String prefix, long count) {
        try {
            final List<HashAtIndex> list = new ArrayList<>();
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            HashAtIndex next = this;
            for (long i = 0; i < count; i++) {
                next = next.nextWith(digest, prefix);
                digest.reset();
                if (next != null)
                    list.add(next);
                else
                    break;
            }
            return list;
        }
        catch (final Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public HashAtIndex nextWith(String prefix) {
        final List<HashAtIndex> list = nextWith(prefix, 1L);
        return list.isEmpty() ? null : list.get(0);
    }

    HashAtIndex nextWith(MessageDigest digest, String prefix) {
        for (long i = index + 1L; i < Long.MAX_VALUE; i += 1L) {
            final String result = convertToMD5ThenHex(digest, input + i);
            if (StringUtils.startsWith(result, prefix)) {
                return new HashAtIndex(input, result, i);
            }
        }

        return null;
    }

    public static String bytesToHex(final byte[] bytes) {
        final char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            final int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    private static String convertToMD5ThenHex(MessageDigest digest, final String text) {
        return bytesToHex(digest.digest(text.getBytes()));
    }
}
